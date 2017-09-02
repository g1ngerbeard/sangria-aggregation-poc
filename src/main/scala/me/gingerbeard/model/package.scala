package me.gingerbeard

import me.gingerbeard.providers.Providers
import sangria.schema._

package object model {

  import sangria.macros.derive._

  @GraphQLName("UserDetail")
  @GraphQLDescription("A user of the system")
  case class UserDetail(@GraphQLDescription("User login")
                        login: String,
                        @GraphQLDescription("User full name")
                        fullName: String,
                        @GraphQLDescription("User birth date")
                        birthDate: String,
                        @GraphQLDescription("User address")
                        address: String,
                        @GraphQLDescription("User avatar ID")
                        avatar: String,
                        @GraphQLDescription("Subscribers of this user")
                        subscribers: Seq[String],
                        @GraphQLDescription(
                          "Channels this user is subscribed to")
                        subscriptions: Seq[String],
                        @GraphQLDescription("Videos added by this user")
                        addedVideos: Seq[String],
                        @GraphQLDescription("Videos liked by this user")
                        likedVideos: Seq[String])

  @GraphQLName("UserDetails")
  @GraphQLDescription("List of user details")
  case class UserDetails(users: Seq[UserDetail])

  case class VideoDetails(id: String,
                          title: String,
                          likes: Int,
                          dislikes: String,
                          watched: Int,
                          date: String,
                          thumbnail: String)

  case class Picture(small: String, large: String)

  case class EnrichedUserDetail(login: String,
                                fullName: String,
                                birthDate: String,
                                address: String,
                                avatar: Picture,
                                subscribers: Seq[EnrichedUserDetail],
                                subscriptions: Seq[EnrichedUserDetail],
                                addedVideos: Seq[VideoDetails],
                                likedVideos: Seq[VideoDetails])

  implicit val UserDetailType = deriveObjectType[Unit, UserDetail]()

  //  implicit val UserDetailsType = deriveObjectType[Unit, UserDetails]

  val AddressArg = Argument("address", OptionInputType(StringType))

  val Query = ObjectType(
    "Query",
    fields[Providers, Unit](
      Field(
        "details",
        ListType(UserDetailType),
        arguments = AddressArg :: Nil,
        resolve = ctx => ctx.ctx.getUserDetails(ctx.arg(AddressArg))
      )
    )
  )

  val RootSchema = Schema(Query)

}
