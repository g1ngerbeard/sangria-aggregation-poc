package me.gingerbeard

import me.gingerbeard.providers.Providers
import sangria.schema._

// todo: partial responses
// todo: authorization?
// todo: filtering of schema
// todo: error handling
package object model {

  import sangria.macros.derive._

  @GraphQLName("UserDetail")
  @GraphQLDescription("A user of the system")
  case class UserDetail(@GraphQLDescription("User login")
                        login: String,
                        @GraphQLDescription("User full name")
                        fullName: String,
                        @GraphQLDescription("User birth date")
                        birthDate: Option[String],
                        @GraphQLDescription("User address")
                        address: String,
                        @GraphQLDescription("User avatar ID")
                        avatar: String,
                        @GraphQLDescription("Subscribers of this user")
                        subscribers: List[String],
                        @GraphQLDescription("Channels this user is subscribed to")
                        subscriptions: List[String],
                        @GraphQLDescription("Videos added by this user")
                        addedVideos: List[String],
                        @GraphQLDescription("Videos liked by this user")
                        likedVideos: List[String])

  @GraphQLName("UserDetails")
  @GraphQLDescription("List of user details")
  case class UserDetails(users: List[UserDetail])

  case class VideoDetails(id: String,
                          title: String,
                          likes: Int,
                          dislikes: Int,
                          watched: Int,
                          date: String,
                          thumbnail: String)

  case class Picture(small: String, large: String)

  implicit val PictureType: ObjectType[Unit, Picture] = deriveObjectType[Unit, Picture]()

  implicit val VideoDetailsType: ObjectType[Unit, VideoDetails] = deriveObjectType[Unit, VideoDetails]()

  //  todo: add data fetchers
  implicit lazy val EnrichedUserDetailType: ObjectType[Providers, UserDetail] =
    deriveObjectType[Providers, UserDetail](
      ReplaceField("subscribers", Field(
        "subscribers",
        ListType(EnrichedUserDetailType),
        resolve = ctx => ctx.ctx.getUserDetails(ctx.value.subscribers)
      )),
      ReplaceField("subscriptions", Field(
        "subscriptions",
        ListType(EnrichedUserDetailType),
        resolve = ctx => ctx.ctx.getUserDetails(ctx.value.subscriptions)
      )),
      ReplaceField("addedVideos", Field(
        "addedVideos",
        ListType(VideoDetailsType),
        resolve = ctx => ctx.ctx.getVideos(ctx.value.addedVideos)
      )),
      ReplaceField("likedVideos", Field(
        "likedVideos",
        ListType(VideoDetailsType),
        resolve = ctx => ctx.ctx.getVideos(ctx.value.likedVideos)
      ))
    )

  val AddressArg = Argument("address", OptionInputType(StringType))

  val Query = ObjectType(
    "Query",
    fields[Providers, Unit](
      Field(
        "details",
        ListType(EnrichedUserDetailType),
        arguments = AddressArg :: Nil,
        resolve = ctx => ctx.ctx.getUserDetails(ctx.arg(AddressArg))
      )
    )
  )

  val RootSchema = Schema(Query)

}
