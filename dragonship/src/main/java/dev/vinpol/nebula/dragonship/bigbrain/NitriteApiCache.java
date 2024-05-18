package dev.vinpol.nebula.dragonship.bigbrain;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.repository.EntityDecorator;
import org.dizitart.no2.repository.EntityId;
import org.dizitart.no2.repository.EntityIndex;
import org.dizitart.no2.repository.ObjectRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

final class NitriteApiCache<K, R> {

    private final Nitrite db;
    private final ObjectRepository<R> collection;

    NitriteApiCache(Nitrite db, Class<R> clazz, String idField) {
        this.db = db;
        this.collection = db.getRepository(new CacheEntityDecorator(clazz, idField));
    }

    R getById(K key) {
        Objects.requireNonNull(key);

        return collection.getById(key);
    }

    Optional<R> getByIdAsOptional(K key) {
        R byId = getById(key);

        return Optional.ofNullable(byId);
    }

    void updateOrInsert(K key, R record) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(record);

        collection.update(record, true);
        db.commit();
    }

    void updateIfExists(K key, Consumer<R> modifier) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(modifier);

        R record = collection.getById(key);

        if (record != null) {
            modifier.accept(record);
            collection.update(record);
        }
    }

    private final class CacheEntityDecorator implements EntityDecorator<R> {
        private final Class<R> clazz;
        private final String idField;

        public CacheEntityDecorator(Class<R> clazz, String idField) {
            this.clazz = clazz;
            this.idField = idField;
        }

        @Override
        public Class<R> getEntityType() {
            return clazz;
        }

        @Override
        public EntityId getIdField() {
            return new EntityId(idField);
        }

        @Override
        public List<EntityIndex> getIndexFields() {
            return List.of();
        }
    }
}
