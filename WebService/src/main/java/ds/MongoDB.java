/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * This program deal with the data exchange with MangoDB
 */
package ds;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB {
    private MongoCollection<Document> collection;
    public String mostRequestCategory = "";
    public String mostRequestCategoryCount = "";
    public String mostRequestDifficulty = "";
    public String mostRequestDifficultyCount = "";
    public String avgLatency = "";

    public MongoDB() {
        // combine the default codec registry, with the PojoCodecProvider configured to automatically create PojoCodecs
        // cite: http://mongodb.github.io/mongo-java-driver/3.7/driver/getting-started/quick-start-pojo/
        try {
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));

            // Replace the uri string with MongoDB deployment's connection string
            String uri = "mongodb+srv://amelia156506:janelee8815@cluster0.lwgrvho.mongodb.net/?retryWrites=true&w=majority";
            ConnectionString connectionString = new ConnectionString(uri);

            MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
            //Creating a MongoDB client
            MongoClient mongoClient = MongoClients.create(settings);
            //Connecting to the database
            MongoDatabase database = mongoClient.getDatabase("myDatabase");
            database = database.withCodecRegistry(pojoCodecRegistry);
            //Connecting to the collection
            collection = database.getCollection("questionsData");
            collection = collection.withCodecRegistry(pojoCodecRegistry);
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }

    }

    public void insert(String resultData, long receiveTime, long responseTime) {
        Gson gson = new Gson();
        Data data = gson.fromJson(resultData, Data.class);
        List<Question> questionList = data.getQuestions();
        // retrieve only one question at a time, get question of index 0
        Question question = questionList.get(0);
        try {
            //Inserting the document into the collection
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId().toString())
                    .append("ResponseCode", data.getResponse_code())
                    .append("Category", question.getCategory())
                    .append("Difficulty", question.getDifficulty())
                    .append("Question", question.getQuestion())
                    .append("CorrectAns", question.getCorrect_answer())
                    .append("WrongAns1", question.getIncorrect_answers().get(0))
                    .append("WrongAns2", question.getIncorrect_answers().get(1))
                    .append("WrongAns3", question.getIncorrect_answers().get(2))
                    .append("ReceivingTime", new Timestamp(receiveTime).toString())
                    .append("ResponseTime", new Timestamp(responseTime).toString())
                    .append("Latency", responseTime - receiveTime)
            );
            System.out.println("Document inserted successfully with id: " + result.getInsertedId());
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
    }

    public List<Log> retrieve() {
        // use an arraylist to store all the logs retrieved from database
        List<Log> logs = new ArrayList<>();

        // use maps to store the frequency of different category and difficulty
        Map<String, Integer> categoryMap = new HashMap<>();
        Map<String, Integer> difficultyMap = new HashMap<>();

        double latency = 0.0;

        try {
            //Retrieving the documents
            FindIterable<Document> iterDoc = collection.find();
            for (Document document : iterDoc) {
                String category = document.getString("Category");
                String difficulty = document.getString("Difficulty");

                // update frequency in map
                categoryMap.put(category, categoryMap.getOrDefault(category, 0) + 1);
                difficultyMap.put(difficulty, difficultyMap.getOrDefault(difficulty, 0) + 1);

                // update latency
                latency += document.getLong("Latency");

                // store the document(an entry) into log list
                Gson gson = new Gson();
                Log log = gson.fromJson(document.toJson(), Log.class);
                logs.add(log);
            }

            // calculate the most frequent asked category and difficulty
            Map.Entry<String, Integer> maxCategory = null;
            for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
                if (maxCategory == null || entry.getValue() > maxCategory.getValue()) {
                    maxCategory = entry;
                }
            }
            Map.Entry<String, Integer> maxDifficulty = null;
            for (Map.Entry<String, Integer> entry : difficultyMap.entrySet()) {
                if (maxDifficulty == null || entry.getValue() > maxDifficulty.getValue()) {
                    maxDifficulty = entry;
                }
            }

            // update the operations analytics data
            if (maxCategory != null) {
                mostRequestCategory = maxCategory.getKey();
                mostRequestCategoryCount = String.valueOf(maxCategory.getValue());
            }

            if (maxDifficulty != null) {
                mostRequestDifficulty = maxDifficulty.getKey();
                mostRequestDifficultyCount = String.valueOf(maxDifficulty.getValue());
            }

            // calculate the average latency
            if (latency != 0.0) {
                avgLatency = String.format("%.2f milliseconds", latency / logs.size());
            }

        } catch (MongoException me) {
            System.err.println("Retrieve failed: " + me);
        }
        return logs;
    }
}
