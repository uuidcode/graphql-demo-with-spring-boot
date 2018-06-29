package com.graphql.study.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graphql.study.demo.dao.UserDao;
import com.graphql.study.demo.model.User;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;

import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLObjectType.newObject;

@RestController
public class UserController {
    private static GraphQLOutputType userType;
    public static GraphQLSchema schema;

    @Autowired
    private UserDao userDao;

    @PostMapping(value = "demo")
    public Object demo(@RequestParam(value = "query", required = false) String query) {
        this.createGraphSchema();

        /*
         * Query example:
         *
         * String query1 = "{users(page:2,size:5,name:\"john\") {id,name}}";
         * String query2 = "{user(id:6) {id,name}}";
         * String query3 = "{user(id:6) {id,name},users(page:2,size:5,name:\"john\") {id,name}}"
         * ;
         */
        Map<String, Object> result = GraphQL.newGraphQL(schema)
            .build()
            .execute(query)
            .getData();

        return result;
    }

    private void initOutputType() {
        userType = newObject().name("User")
            .field(GraphQLFieldDefinition.newFieldDefinition().name("userId").type(Scalars.GraphQLLong).build())
            .field(GraphQLFieldDefinition.newFieldDefinition().name("age").type(Scalars.GraphQLInt).build())
            .field(GraphQLFieldDefinition.newFieldDefinition().name("name").type(Scalars.GraphQLString).build())
            .build();
    }

    private GraphQLFieldDefinition createUserField() {
        GraphQLArgument userIdArgument = newArgument()
            .name("userId")
            .type(Scalars.GraphQLLong)
            .build();

        return GraphQLFieldDefinition.newFieldDefinition()
            .name("user")
            .argument(userIdArgument)
            .type(userType)
            .dataFetcher(this::getUser).build();
    }

    private GraphQLFieldDefinition createUsersField() {
        return GraphQLFieldDefinition.newFieldDefinition().name("users")
            .argument(newArgument().name("page").type(Scalars.GraphQLInt).build())
            .argument(newArgument().name("size").type(Scalars.GraphQLInt).build())
            .argument(newArgument().name("name").type(Scalars.GraphQLString).build()).type(new GraphQLList(userType))
            .dataFetcher(environment -> {
                int page = environment.getArgument("page");
                int size = environment.getArgument("size");
                String name = environment.getArgument("name");

                List<User> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    User user = new User();
                    user.setUserId(new Long(i));
                    user.setAge(i);
                    user.setName("Name_" + i);
                    list.add(user);
                }
                return list;
            }).build();
    }

    private void createGraphSchema() {
        initOutputType();

        GraphQLObjectType graphQuery = newObject()
            .name("GraphQuery")
            .field(createUsersField())
            .field(createUserField())
            .build();

        schema = GraphQLSchema.newSchema()
            .query(graphQuery).build();

    }

    private Object getUser(DataFetchingEnvironment environment) {
        Long userId = environment.getArgument("userId");
        return userDao.getUser(userId);
    }
}
