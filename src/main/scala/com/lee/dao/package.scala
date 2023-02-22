package com.lee

import org.springframework.data.mongodb.core.{MongoTemplate, SimpleMongoClientDatabaseFactory}

package object dao {
  private[dao] var mongodbTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/test_db"))
}
