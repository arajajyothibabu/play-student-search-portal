import dao.StudentDAOMongo
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import router.Routes
import play.api.ApplicationLoader.Context
import play.api.{ApplicationLoader, BuiltInComponentsFromContext}
import reactivemongo.api.{MongoConnection, MongoDriver}
import services.StudentService

import scala.concurrent.Future

/**
  * Created by jyothi on 17/4/17.
  */
class NPSApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context) {

  implicit val ec = materializer.executionContext

  lazy val serverHost: String = configuration.getString("play.server.http.host").get
  lazy val serverPort: String = configuration.getString("play.server.http.port").get

  lazy val sparkMaster: String = configuration.getString("spark.master").get
  lazy val sparkAppName: String = configuration.getString("spark.appName").get
  lazy val sparkAppId: String = configuration.getString("spark.app.id").get
  lazy val sparkCassandraConnectionHost: String = configuration.getString("spark.cassandra.connection.host").get
  lazy val sparkStreamingPollSeconds: Int = configuration.getInt("spark.streaming.seconds").get

  lazy val kafkaPartitions: Int = configuration.getInt("kafka.partitions").get

  lazy val mongoDBURI: String = configuration.getString("mongo.db.uri").get

  val driver = new MongoDriver
  //creating database connection
  lazy val mongoConnection: MongoConnection = MongoConnection.parseURI(mongoDBURI).map(driver.connection).get

  //removing database connnection after application stopped
  applicationLifecycle.addStopHook(() =>
    Future(mongoConnection.close())
  )

  lazy val studentDAOMongo = new StudentDAOMongo(mongoConnection)
  lazy val studentService = new StudentService(studentDAOMongo)

  lazy val applicationController = new controllers.Application(studentService, serverHost)
  lazy val assets = new controllers.Assets(httpErrorHandler)

  lazy val router = new Routes(httpErrorHandler, applicationController, assets)

}
