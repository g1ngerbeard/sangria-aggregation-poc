package me.gingerbeard.providers

import me.gingerbeard.model._

import scala.concurrent.{ExecutionContext, Future}

class Providers {

  import ExecutionContext.Implicits._

  // mock data

  val pictures: Map[String, Picture] = Map()

  val users: Map[String, UserDetail] = Map(
    "agent007" -> UserDetail(
      login = "agent007",
      fullName = "Little Nerd",
      birthDate = "2005",
      address = "Kiev",
      avatar = "a1",
      subscribers = Seq.empty,
      subscriptions = Seq("popular_blogger"),
      addedVideos = Seq.empty,
      likedVideos =
        Seq("travel1", "travel2", "travel3", "video4", "stupidvideo")
    ),
    "popular_blogger" -> UserDetail(
      login = "popular_blogger",
      fullName = "Hans Larlson",
      birthDate = "1990",
      address = "Berlin",
      avatar = "b1",
      subscribers = Seq("agent007", "kitty"),
      subscriptions = Seq(),
      addedVideos = Seq("travel1", "travel2", "travel3", "video4"),
      likedVideos = Seq()
    ),
    "kitty" -> UserDetail(
      login = "kitty",
      fullName = "Kate Grushenko",
      birthDate = "1999",
      address = "Kiev",
      avatar = "k1",
      subscribers = Seq("agent007", "friendofkitty"),
      subscriptions = Seq("popular_blogger", "friendofkitty"),
      addedVideos = Seq("stupidvideo1"),
      likedVideos = Seq("travel1", "stupidvideo2")
    ),
    "friendofkitty" -> UserDetail(
      login = "friendofkitty",
      fullName = "Max Muller",
      birthDate = "1999",
      address = "Berlin",
      avatar = "max1",
      subscribers = Seq("kitty"),
      subscriptions = Seq("kitty"),
      addedVideos = Seq("stupidvideo2"),
      likedVideos = Seq("stupidvideo1")
    )
  )

  val videos: Map[String, VideoDetails] = Map()

  // emulate calls to data provider service

  def getUserLogins(address: String): Future[Seq[String]] =
    Future(users.values.filter(_.address.contains(address)).map(_.login).toSeq)

  def getUserDetails(address: Option[String]): Future[List[UserDetail]] =
    Future {
      address
        .map(a => users.values.filter(_.address.contains(a)))
        .getOrElse(users.values)
        .toList
    }

  def getPicture(id: String): Future[Picture] = Future(pictures(id))

  def getUserDetail(id: String): Future[UserDetail] = Future(users(id))

  def getUserDetails(ids: Seq[String]): Future[Seq[UserDetail]] =
    Future(ids.map(users))

  def getVideo(id: String): Future[VideoDetails] = Future(videos(id))

}
