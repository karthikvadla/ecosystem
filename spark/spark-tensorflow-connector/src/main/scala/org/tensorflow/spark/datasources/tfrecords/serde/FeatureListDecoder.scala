package org.tensorflow.spark.datasources.tfrecords.serde

import org.tensorflow.example.{ FeatureList}

trait FeatureListDecoder[T<:Serializable] extends Serializable{
  /**
    * Decodes each TensorFlow "FeatureList" to desired Scala type
    *
    * @param featureList TensorFlow FeatureList
    * @return Decoded featureList
    */
  def decode(featureList: FeatureList): T
}
