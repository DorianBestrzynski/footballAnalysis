package com.masterthesis.footballanalysis.version_1.repository;


import com.masterthesis.footballanalysis.version_1.dto.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository("MongoDbReadRepositoryV1")
@Slf4j
@RequiredArgsConstructor
public class MongoDbReadRepository {
    private final MongoDatabase database;

    public List<Query1DTOMongo> query1() {
        MongoCollection<Document> collection = database.getCollection("GithubData");
        FindIterable<Document> result = collection.find(new Document("followers", new Document("$eq", 0)))
                .projection(new Document("id", 1L)
                        .append("name", 1L)
                        .append("login", 1L));

        List<Query1DTOMongo> query1List = new ArrayList<>();
        for (Document doc : result) {
            Query1DTOMongo query1 = new Query1DTOMongo();
            query1.setUserId(doc.getInteger("id"));
            query1.setName(doc.getString("name"));
            query1.setLogin(doc.getString("login"));
            query1List.add(query1);
        }
        return query1List;
    }

    public List<Query1DTOMongo> query1_2() {
        MongoCollection<Document> collection = database.getCollection("GithubData");
        FindIterable<Document> result = collection.find(new Document("id", 763))
                .projection(new Document("id", 1)
                                .append("name", 1)
                                .append("login", 1));

        List<Query1DTOMongo> query1List = new ArrayList<>();
        for (Document doc : result) {
            Query1DTOMongo query1 = new Query1DTOMongo();
            query1.setUserId(doc.getInteger("id"));
            query1.setName(doc.getString("name"));
            query1.setLogin(doc.getString("login"));
            query1List.add(query1);
        }
        return query1List;
    }

    public List<Query2DTO> query2() {
        MongoCollection<Document> collection = database.getCollection("GithubData");
        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("id", 856)),
                new Document("$unwind", "$commit_list"),
                new Document("$project", new Document("message", "$commit_list.message")
                        .append("name", 1L)));

        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query2DTO> query2List = new ArrayList<>();
        for (Document doc : result) {
            Query2DTO query2 = new Query2DTO();
            query2.setAuthorName(doc.getString("name"));
            query2.setCommitMessage(doc.getString("message"));
            query2List.add(query2);
        }
        return query2List;
    }

    public List<Query2DTO> query2_2() {
        MongoCollection<Document> collection = database.getCollection("GithubData");
        List<Document> pipeline = Arrays.asList(
                new Document("$unwind", "$commit_list"),
                new Document("$project", new Document("message", "$commit_list.message")),
                new Document("$limit", 1000000));

        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query2DTO> query2List = new ArrayList<>();
        for (Document doc : result) {
            Query2DTO query2 = new Query2DTO();
            query2.setCommitMessage(doc.getString("message"));
            query2List.add(query2);
        }
        return query2List;
    }

    public List<Query3DTOMongo> query3() {
        MongoCollection<Document> collection = database.getCollection("GithubData");
        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("id", 704)),
                new Document("$unwind", "$repo_list"),
                new Document("$project", new Document("id", 1L)
                        .append("name", 1L)
                        .append("repo_id", "$repo_list.id")
                        .append("repo_name", "$repo_list.full_name")));

        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query3DTOMongo> query3DTOMongoList = new ArrayList<>();
        for (Document doc : result) {
            Query3DTOMongo query3 = new Query3DTOMongo();
            query3.setUserId(doc.getInteger("id"));
            query3.setUserName(doc.getString("name"));
            query3.setRepoId(doc.getInteger("repo_id"));
            query3.setRepoName(doc.getString("repo_name"));
            query3DTOMongoList.add(query3);
        }
        return query3DTOMongoList;
    }

    public List<Document> query4() {
        MongoCollection<Document> collection = database.getCollection("GithubData");

        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("id", 380)),
                new Document("$project", new Document("user_id", 1L)
                        .append("name", 1L)
                        .append("follower_list", 1L)
                        .append("following_list", 1L)
                        .append("repo_list", 1L)
                        .append("commit_list", 1L)));


        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Document> resultList = new ArrayList<>();
        for (Document doc : result) {
            resultList.add(doc);
        }
        return resultList;
    }

    public List<Query4v2DTO> query4v2() {
        MongoCollection<Document> collection = database.getCollection("GithubData");

        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("id", 282)),
                new Document("$unwind", "$repo_list"),
                new Document("$lookup", new Document("from", "GithubData")
                        .append("localField", "repo_list.id")
                        .append("foreignField", "commit_list.repo_id")
                        .append("as", "commits")),
                new Document("$project", new Document("id", 1L)
                        .append("name", 1L)
                        .append("repo_id", "$repo_list.id")
                        .append("repo_name", "$repo_list.full_name")
                        .append("commit_id", "$commits.commit_list.commit_id")
                        .append("commit_message", "$commits.commit_list.message")));


        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query4v2DTO> query3DTOMongoList = new ArrayList<>();

        for (Document doc : result) {
            Query4v2DTO query4 = new Query4v2DTO();
            query4.setUserId(doc.getInteger("id"));
            query4.setUserName(doc.getString("name"));
            query4.setRepoId(doc.getInteger("repo_id"));
            query4.setRepoName(doc.getString("repo_name"));
            query3DTOMongoList.add(query4);
        }
        return query3DTOMongoList;
    }

    public List<Query5DTO> query5() {
        MongoCollection<Document> collection = database.getCollection("GithubData");

        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("location", "New York")),
                new Document("$group", new Document("_id", "$location")
                        .append("user_count", new Document("$sum", 1))),
                new Document("$sort", new Document("user_count", -1)), // Sort by user_count descending
                new Document("$limit", 100));


        // Execute the aggregation pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        List<Query5DTO> query3DTOMongoList = new ArrayList<>();

        for (Document doc : result) {
            Query5DTO query5 = new Query5DTO();
            query5.setLocation(doc.getString("_id"));
            query5.setUserCount(doc.getInteger("user_count"));
            query3DTOMongoList.add(query5);
        }
        return query3DTOMongoList;
    }

    public List<Document> query6() {
        MongoCollection<Document> collection = database.getCollection("GithubData");


        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("id", 176)),
                // Unwind repo_list array
        new Document("$unwind", "$repo_list"),
                // Unwind commit_list array
                new Document("$unwind", "$commit_list"),
                // Match documents to filter out null commit dates
                new Document("$match", new Document("commit_list.commit_at", new Document("$ne", null))),
                // Project required fields and convert commit_at to date and truncate to month
                new Document("$project", new Document("id", 1)
                        .append("name", 1)
                        .append("repo_id", "$repo_list.id")
                        .append("repo_name", "$repo_list.full_name")
                        .append("commit_month", new Document("$dateTrunc", new Document("date", new Document("$dateFromString", new Document("dateString", "$commit_list.commit_at"))).append("unit", "month")))),
                // Group by user_id, name, repo_id, repo_name, commit_month and count commits
                new Document("$group", new Document("_id", new Document("id", "$id")
                        .append("name", "$name")
                        .append("repo_id", "$repo_id")
                        .append("repo_name", "$repo_name")
                        .append("commit_month", "$commit_month"))
                        .append("commit_count", new Document("$sum", 1))),
                // Sort the results by user_id, repo_id, commit_month
                new Document("$sort", new Document("_id.id", 1)
                        .append("_id.repo_id", 1)
                        .append("_id.commit_month", 1)));

        AggregateIterable<Document> result = collection.aggregate(pipeline);
        List<Document> resultList = new ArrayList<>();
        for (Document doc : result) {
            resultList.add(doc);
        }
        return resultList;
    }
}
