import com.vapi4k.api.json.toJsonString
import com.vapi4k.common.CoreEnvVars.deepGramVoiceIdType
import com.vapi4k.common.JsonContentUtils.defaultJson
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.utils.HttpUtils.jsonHttpClient
import com.vapi4k.utils.HttpUtils.wsHttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

@Serializable
class DeepGramProperties(
  @EncodeDefault
  var callback: String = "",
  @EncodeDefault
  @SerialName("callback_method")
  var callbackMethod: Boolean = false,
  @EncodeDefault
  var version: String = "latest",
  @EncodeDefault
  var model: String = "nova-2",
  @EncodeDefault
  var language: String = "en-US",
  @EncodeDefault
  var encoding: String = "linear16",
  @EncodeDefault
  @SerialName("sample_rate")
  var sampleRate: Int = 16000,
  @EncodeDefault
  var channels: Int = 2,
  @EncodeDefault
  @SerialName("smart_format")
  var smartFormat: Boolean = true,
)

fun main() {
  /*
  curl \
  --request POST \
  --header 'Authorization: Token YOUR_DEEPGRAM_API_KEY' \
  --header 'Content-Type: application/json' \
  --data '{"url":"https://static.deepgram.com/examples/interview_speech-analytics.wav"}' \
  --url 'https://api.deepgram.com/v1/listen?model=nova-2&smart_format=true'

   */

  @Serializable
  data class DeepGramRequest(
    @EncodeDefault
    val url: String = "https://static.deepgram.com/examples/interview_speech-analytics.wav",
  )

  val resp =
    jsonHttpClient().use { client ->
      runBlocking {
        client.post("https://api.deepgram.com/v1/listen?model=nova-2&smart_format=true") {
          contentType(Application.Json)
          headers.append(HttpHeaders.Authorization, "Token $deepGramVoiceIdType")
          val props = DeepGramRequest()
          setBody(props)
        }
      }
    }

  runBlocking {
    println(resp.status)
    val text = resp.bodyAsText()
    println(text.toJsonString())
  }
}

object SpeachListener {
  val websocketActions = ConcurrentHashMap<String, Thread>()

  fun listenTo(listenUrl: String) {
    if (listenUrl.isNotBlank()) {
      websocketActions.computeIfAbsent(listenUrl) {
        thread {
          println("Starting websocket listener for $listenUrl")
          runBlocking {
            delay(1000)
            val dataChannel = Channel<Pair<Boolean, ByteArray>>()
            coroutineScope {
              launch {
                runCatching {
                  connectToVapiWS(listenUrl, dataChannel)
                }.onFailure {
                  logger.error(it) { "Error listening to $listenUrl" }
                }
              }

              launch {
                runCatching {
                  connectToDeepgramWS(dataChannel)
                }.onFailure {
                  logger.error(it) { "DG Error ${it.message}" }
                }
              }
            }
          }
        }
      }
    }
  }

  private suspend fun connectToVapiWS(
    url: String,
    dataChannel: Channel<Pair<Boolean, ByteArray>>,
  ) {
    wsHttpClient().use { client ->
      val pcmBuffer = mutableListOf<ByteArray>()
      client.webSocket(urlString = url) {
        println("Vapi WebSocket connection established")
        for (frame in incoming) {
          when (frame) {
            is Frame.Binary -> {
              val data = frame.readBytes()
              dataChannel.send(false to data)
              pcmBuffer.add(data)
              println("Received PCM data, buffer size: ${pcmBuffer.sumOf { it.size }}")
            }

            is Frame.Text -> {
              println("Received message: ${frame.readText()}")
            }

            is Frame.Ping -> {
              println("Received ping")
            }

            is Frame.Pong -> {
              println("Received pong")
            }

            is Frame.Close -> {
              println("Connection closed")
              break
            }
          }
        }
        dataChannel.send(true to ByteArray(0))

        println("Vapi WebSocket connection closed")

        // write pcmBuffer to file
        if (pcmBuffer.isNotEmpty()) {
          val audioData = pcmBuffer.map { it.toList() }.flatten().toByteArray()
          File("audio.pcm").writeBytes(audioData)
          println("Audio data saved to audio.pcm")
        }
      }
    }
  }

  private suspend fun connectToDeepgramWS(dataChannel: Channel<Pair<Boolean, ByteArray>>) {
    println("Deepgram connection taking place")
    wsHttpClient {
      install(ContentNegotiation) { json(defaultJson()) }
    }.use { client ->
      // "wss://api.deepgram.com/v1/listen"
      // utterance_end_ms=1000&vad_events=true&interim_results=true
      val url = "wss://api.deepgram.com/v1/listen"
      val args = "model=nova-2&smart_format=true&encoding=linear16&channels=2&sample_rate=16000&multichannel=true"
      client.webSocket(
        urlString = "$url?$args",
        // method = io.ktor.http.HttpMethod.Post,
        request = {
          // //   method = io.ktor.http.HttpMethod.Post
          // url.protocol = URLProtocol.WSS
          // url.host = "api.deepgram.com"
          // url.encodedPath = "/v1/listen"

          //   contentType(Application.Json)
          headers.append(HttpHeaders.Authorization, "Token $deepGramVoiceIdType")
          // val props = DeepGramProperties()
          // setBody(props)
        },
      ) {
        println("Deepgram WebSocket connection established")
        coroutineScope {
          launch {
            var total = 0
            for (data in dataChannel) {
              total += data.second.size
              println("Sending DG binary data ${data.second.size} / $total")
              send(Frame.Binary(data.first, data.second))
            }
          }

          launch {
            for (frame in incoming) {
              when (frame) {
                is Frame.Binary -> {
                  val data = frame.readBytes()
                  println("Received DG binary data")
                }

                is Frame.Text -> {
                  println("Received DG message: ${frame.readText().toJsonString()}")
                }

                is Frame.Ping -> {
                  println("Received DG ping")
                }

                is Frame.Pong -> {
                  println("Received DG pong")
                }

                is Frame.Close -> {
                  println("Connection DG closed")
                  break
                }
              }
            }
            println("Deepgram WebSocket connection closed")
          }
        }
      }
    }
  }
}
