package me.gingerbeard.providers

import me.gingerbeard.model._

import scala.concurrent.{ExecutionContext, Future}

// todo: add failing provider
class Providers {

  import ExecutionContext.Implicits._

  // mock data

  val pictures: Map[String, Picture] = Map()

  val users: Map[String, UserDetail] = Map(
    "agent007" -> UserDetail(
      login = "agent007",
      fullName = "Little Nerd",
      birthDate = Some("2005"),
      address = "Kiev",
      avatar = "a1",
      subscribers = List.empty,
      subscriptions = List("popular_blogger"),
      addedVideos = List.empty,
      likedVideos =
        List("1", "2", "3", "4", "6")
    ),
    "popular_blogger" -> UserDetail(
      login = "popular_blogger",
      fullName = "Hans Larlson",
      birthDate = Some("1990"),
      address = "Berlin",
      avatar = "b1",
      subscribers = List("agent007", "kitty"),
      subscriptions = List.empty,
      addedVideos = List("2", "3", "4", "5"),
      likedVideos = List.empty
    ),
    "kitty" -> UserDetail(
      login = "kitty",
      fullName = "Kate Grushenko",
      birthDate = Some("1999"),
      address = "Kiev",
      avatar = "k1",
      subscribers = List("agent007", "friendofkitty"),
      subscriptions = List("popular_blogger", "friendofkitty"),
      addedVideos = List("1"),
      likedVideos = List("2", "6")
    ),
    "friendofkitty" -> UserDetail(
      login = "friendofkitty",
      fullName = "Max Muller",
      birthDate = Some("1999"),
      address = "Berlin",
      avatar = "max1",
      subscribers = List("kitty"),
      subscriptions = List("kitty"),
      addedVideos = List("6"),
      likedVideos = List("1")
    )
  )

  val videos: Map[String, VideoDetails] = Map(
    "1" -> VideoDetails(
      id = "1",
      title = "stupidvideo1",
      likes = 12,
      dislikes = 200,
      watched = 1000,
      date = "12-12-2012",
      thumbnail = "123"
    ),
    "2" -> VideoDetails(
      id = "2",
      title = "travel1",
      likes = 12000,
      dislikes = 100,
      watched = 200000,
      date = "12-01-2013",
      thumbnail = "123"
    ),
    "3" -> VideoDetails(
      id = "3",
      title = "travel2",
      likes = 10000,
      dislikes = 200,
      watched = 100000,
      date = "12-01-2014",
      thumbnail = "123"
    ),
    "4" -> VideoDetails(
      id = "4",
      title = "travel3",
      likes = 3000,
      dislikes = 100,
      watched = 100000,
      date = "12-01-2015",
      thumbnail = "123"
    ),
    "5" -> VideoDetails(
      id = "5",
      title = "video4",
      likes = 12000,
      dislikes = 2000,
      watched = 100000,
      date = "12-01-2016",
      thumbnail = "123"
    ),
    "6" -> VideoDetails(
      id = "6",
      title = "stupidvideo2",
      likes = 1200,
      dislikes = 10000,
      watched = 230000,
      date = "07-07-2017",
      thumbnail = "123"
    )
  )

  // emulate calls to data provider service

  def getUserLogins(address: String): Future[List[String]] =
    Future(users.values.filter(_.address.contains(address)).map(_.login).toList)

  def getUserDetails(address: Option[String]): Future[List[UserDetail]] =
    Future {
      address
        .map(a => users.values.filter(_.address.contains(a)))
        .getOrElse(users.values)
        .toList
    }

  def getPicture(id: String): Future[Picture] = Future(pictures(id))

  def getUserDetail(id: String): Future[UserDetail] = Future(users(id))

  def getUserDetails(ids: List[String]): Future[List[UserDetail]] =
    Future(ids.map(users))

  def getVideos(ids: List[String]): Future[List[VideoDetails]] = Future(ids.map(videos).toList)

}
