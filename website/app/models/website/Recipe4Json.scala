package models.website

// import just Reads helpers in scope
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError

import models.{ Recipe }
import models.dao.{ IngredientDao }
import Json._

object Recipe4Json {

  /**
   * Deserializer for Map[String,V] types.
   */
  implicit def mapIntReads[V](implicit fmtv: Reads[V]): Reads[Map[Long, V]] = new Reads[Map[Long, V]] {

    private def parseLong(s: String) = try { Some(s.toLong) } catch { case _: Throwable => None }

    def reads(json: JsValue) = json match {
      case JsObject(m) => {
        // first validates prod separates JsError / JsResult in an Seq[Either( (key, errors, globals), (key, v, jselt) )]
        // the aim is to find all errors prod then to merge them all
        var hasErrors = false

        val r = m.map {
          case (key, value) =>
            val keyId = parseLong(key)
            fromJson[V](value)(fmtv) match {
              case JsSuccess(v, _) if keyId.isDefined => Right((keyId.get, v, value))
              case JsSuccess(v, _) if keyId.isEmpty =>
                hasErrors = true
                val res = Seq((JsPath \ key) -> Seq(ValidationError("validate.error.expected.key.int")))
                Left(res)
              case JsError(e) =>
                hasErrors = true
                val res = e.map { case (p, valerr) => (JsPath \ key) ++ p -> valerr }
                Left(res)
            }
        }

        // if errors, tries to merge them into a single JsError
        if (hasErrors) {
          val fulle = r.filter(_.isLeft)
            .map(_.left.get)
            .foldLeft {
              (List[(JsPath, Seq[ValidationError])]())
            }((acc, v) => acc ++ v)
          JsError(fulle)
        } // no error, rebuilds the map
        else JsSuccess(r.filter(_.isRight).map(_.right.get).map { v => v._1 -> v._2 }.toMap)
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("validate.error.expected.jsobject"))))
    }
  }

  // -- Parsers
  /**
   * Parse a Recipe from JSon data
   */
  implicit val recipeJsonReads = {
    ((__ \ "name").read[String] and
      (__ \ "instructions").read[String] and
      (__ \ "author").read[Option[String]] and
      (__ \ "isPublic").read[Boolean] and
      (__ \ "description").read[String] and
      (__ \ "prepTimeSec").read[Int] and
      (__ \ "cookTimeSec").read[Int] and
      (__ \ "ingredientsIdQt").read[Map[Long, Double]](mapIntReads[Double])) {
        (name, instructions, author, isPublic, description,
        prepTimeSec, cookTimeSec, ingredientsIdQt) =>
          val ingredientsId =
            for (
              (id, qt) <- ingredientsIdQt;
              ingredientOpt = IngredientDao.findById(id) if (ingredientOpt.isDefined);
              ingredient <- ingredientOpt
            ) yield {
              ingredient -> qt
            }
          Recipe(name, instructions, author, isPublic,
            description, prepTimeSec, cookTimeSec, ingredientsId)
      }
  }
}