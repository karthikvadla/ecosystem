package org.tensorflow.spark.datasources.tfrecords.serde

import org.tensorflow.example.{ FeatureList}

trait FeatureListEncoder extends Serializable{

  /**
    * Encodes input value as TensorFlow "FeatureList"
    *
    * Maps input value to one of feature_list of features of type Int64List, FloatList, BytesList
    *
    * @param value Input value
    * @return TensorFlow FeatureList
    */
  def encode(value: Any): FeatureList
}

/**
  * Encode input value to Int64List
  */
object Int64FeatureListEncoder extends FeatureListEncoder {
  override def encode(value: Any): FeatureList = {
    try {
      val int64List = value match {
        case arr: scala.collection.mutable.WrappedArray[_] => toInt64List(arr.toArray[Any])
        case arr: Array[_] => toInt64List(arr)
        case seq: Seq[_] => toInt64List(seq.toArray[Any])
        case _ => throw new RuntimeException(s"Cannot convert object $value to Int64List")
      }
      FeatureList.newBuilder().setInt64List(int64List).build()
    }
    catch {
      case ex: Exception =>
        throw new RuntimeException(s"Cannot convert object $value of type ${value.getClass} to Int64List feature.", ex)
    }
  }

  private def toInt64List[T](arr: Array[T]): Int64List = {
    val intListBuilder = Int64List.newBuilder()
    arr.foreach(x => {
      require(x != null, "Int64List with null values is not supported")
      val longValue = DataTypesConvertor.toLong(x)
      intListBuilder.addValue(longValue)
    })
    intListBuilder.build()
  }
}
