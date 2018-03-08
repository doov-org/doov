package io.doov.benchmark.model;

import static io.doov.core.dsl.runtime.FieldChainBuilder.from;

import java.util.ArrayList;
import java.util.List;

import io.doov.core.dsl.runtime.RuntimeField;
import io.doov.core.dsl.runtime.RuntimeFieldRegistry;

public class RuntimePaths extends RuntimeFieldRegistry<BenchmarkModel> {

    private static List<RuntimeField<BenchmarkModel, Object>> ALL = new ArrayList<>();

    public static RuntimeField<BenchmarkModel, Integer> age = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.AGE)
                    .readable("driver age")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .field(Driver::getAge, Driver::setAge, Integer.TYPE)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> name = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.NAME)
                    .readable("driver age")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .field(Driver::getName, Driver::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, Boolean> drivingLicense = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.DRIVING_LICENSE)
                    .readable("driver license")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .field(Driver::isHasDrivingLicense, Driver::setHasDrivingLicense, Boolean.TYPE)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_1 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_1)
                    .readable("friend name 1")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 1, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_2 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_2)
                    .readable("friend name 2")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 2, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_3 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_3)
                    .readable("friend name 3")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 3, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_4 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_4)
                    .readable("friend name 4")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 4, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_5 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_5)
                    .readable("friend name 5")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 5, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_6 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_6)
                    .readable("friend name 6")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 6, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_7 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_7)
                    .readable("friend name 7")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 7, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_8 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_8)
                    .readable("friend name 8")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 8, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimeField<BenchmarkModel, String> friend_name_9 = from(BenchmarkModel.class)
                    .fieldId(BenchmarkFieldId.FRIEND_NAME_9)
                    .readable("friend name 9")
                    .get(BenchmarkModel::getDriver, BenchmarkModel::setDriver, Driver::new)
                    .list(Driver::getFriends, Driver::setFriends, 9, Friend::new)
                    .field(Friend::getName, Friend::setName, String.class)
                    .register(ALL);

    public static RuntimePaths INSTANCE = new RuntimePaths();

    public RuntimePaths() {
        super(ALL);
    }

}
