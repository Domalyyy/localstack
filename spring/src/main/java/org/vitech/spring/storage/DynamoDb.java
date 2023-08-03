package org.vitech.spring.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.vitech.spring.model.Person;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Repository
@RequiredArgsConstructor
public class DynamoDb {
    private final DynamoDbTable<Person> personTable;

    public void save(Person person) {
        personTable.putItem(person);
    }
}
