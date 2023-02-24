package com.lee

import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoClientDatabaseFactory}

package object dao {
  private[dao] val mongodbTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/test_db"))
}
