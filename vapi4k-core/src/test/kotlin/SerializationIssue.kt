import com.github.pambrose.common.json.toJsonString
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Serializable(with = ChildSerializer::class)
sealed interface Child

object ChildSerializer : KSerializer<Child> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Child")

  override fun serialize(
    encoder: Encoder,
    value: Child,
  ) {
    encoder.encodeSerializableValue(DataChild.serializer(), value as DataChild)
  }

  override fun deserialize(decoder: Decoder): Child = throw NotImplementedError("Deserialization is not supported")
}

val module = SerializersModule {
  polymorphic(Parent::class) {
    subclass(DataChild::class)
    subclass(NonDataChild::class)
  }
}

@Serializable
abstract class Parent {
  @EncodeDefault
  var name: String = ""
}

@Serializable
data class DataChild(
  var street: String = "",
) : Parent()

@Serializable
class NonDataChild(
  var street: String = "",
) : Parent()

@Serializable
class Family(
  val dataChild: DataChild = DataChild(),
  val nonDataChild: NonDataChild = NonDataChild(),
)

val format = Json { serializersModule = module }

private inline fun <reified T> T.toJsonElement2() = format.encodeToJsonElement(this)

fun main() {
  val c = Family().apply {
    dataChild.name = "Bill"
    nonDataChild.name = "Bob"
//    dataChild.street = "Bill Street"
//    nonDataChild.street = "Bob Street"
  }

  println(c.toJsonElement2().toJsonString())

  val d = DataChild().apply {
    name = "Bill"
  }

  println(d.toJsonElement2().toJsonString())

  val e = NonDataChild().apply {
    name = "Bob"
  }

  println(e.toJsonElement2().toJsonString())
}
