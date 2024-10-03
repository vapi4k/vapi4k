import com.vapi4k.api.json.toJsonElement
import com.vapi4k.api.web.MethodType
import com.vapi4k.dsl.web.VapiWeb.vapiTalkButton
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title

object TalkPage {
  fun HTML.talkPage() {
    head {
      title { +"Talk with an Assistant" }
    }
    body {
      h1 { +"Talk with an Assistant" }
      vapiTalkButton {
        serverPath = "/talkSquad"
        serverSecret = "12345"
        method = MethodType.POST
        postArgs = mapOf(
          "x" to "1",
          "y" to "2",
          "name" to "Bill",
        ).toJsonElement()
      }
    }
  }
}
