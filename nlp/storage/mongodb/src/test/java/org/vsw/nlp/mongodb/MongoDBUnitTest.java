package org.vsw.nlp.mongodb;

import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import org.vsw.nlp.topic.storage.InputStreamTopicIterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoDBUnitTest {
	@Test
	public void test() throws Exception {
		Mongo m = new Mongo( "192.168.2.11" , 27017 );
		DB db = m.getDB( "nlp" );
		DBCollection coll = db.getCollection("place") ;
		coll.remove(new BasicDBObject()) ;
		InputStreamTopicIterator itr = 
			new InputStreamTopicIterator(Thread.currentThread().getContextClassLoader().getResourceAsStream("data.place.json")) ;
		JsonNode jnode = null ;
		while((jnode = itr.next()) != null) {
			DBObject dbobject = (DBObject)JSON.parse(jnode.toString()) ;
			DBObject save = new BasicDBObject() ;
			save.put("otype", "place") ;
			save.put("place", dbobject) ;
			coll.save(save) ;
		}
		
		Set<String> colls = db.getCollectionNames();
		for (String s : colls) {
			System.out.println(s);
		}
		DBCursor cursor = coll.find().limit(10) ;
		while(cursor.hasNext()) {
			System.out.println(cursor.next().toString());
		}
	}
}
