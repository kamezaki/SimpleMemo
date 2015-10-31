package biz.info_cloud.simplememo.repository.realm;

public interface DataMapper<D, R> {
    D mapToDomain(R realmObject);
    R mapToRealm(D domainObject);
}
