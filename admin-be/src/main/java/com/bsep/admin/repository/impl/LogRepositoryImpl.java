package com.bsep.admin.repository.impl;

import com.bsep.admin.model.Log;
import com.bsep.admin.model.Message;
import com.bsep.admin.repository.CustomLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogRepositoryImpl implements CustomLogRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

   /* @Override
    public void keep100MostRecentMessages() {
        // create a query that sorts by timestamp descending and limits to 100
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "timestamp")).limit(100);
        // find the 100 most recent messages
        List<Log> messages = mongoTemplate.find(query, Log.class);
        // delete all messages
        mongoTemplate.remove(new Query(), Log.class);
        // save the 100 most recent messages
        mongoTemplate.insert(messages, Log.class);
    }*/
   @Override
   public void keep100MostRecentMessages() {
       // create a query that sorts by timestamp descending and limits to 100
       Query query = new Query().with(Sort.by(Sort.Direction.DESC, "timestamp")).limit(100);
       // find the 100 most recent messages
       List<Log> messages = mongoTemplate.find(query, Log.class);
       // delete all messages
       mongoTemplate.remove(new Query(), Log.class);
       // save the 100 most recent messages
       for (Log message : messages) {
           mongoTemplate.save(message);
       }
   }
}
