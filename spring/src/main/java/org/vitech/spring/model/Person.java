package org.vitech.spring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
//import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
//import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@AllArgsConstructor
@NoArgsConstructor
@Builder
//@DynamoDbBean
@Setter
public class Person {
    private String id;
    private String firstName;
    private String lastName;

//    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

//    @DynamoDbAttribute("first_name")
    public String getFirstName() {
        return firstName;
    }

//    @DynamoDbAttribute("last_name")
    public String getLastName() {
        return lastName;
    }
}
