Dropwizard jOOQ Bundle
======================
An addon bundle for using the jOOQ SQL library in Dropwizard applications.


Dependency Info
---------------

```xml
<dependency>
	<groupId>ai.dot.dwtools</groupId>
	<artifactId>dw-jooq</artifactId>
	<version>1.0.0-x</version>
</dependency>
```

Usage
-----

Add a `JooqBundle` to your `Application` class.

```java
@Override
public void initialize(Bootstrap<MyConfiguration> bootstrap) {
    // ...
    bootstrap.addBundle(new JooqBundle<AppConfig>() {
        @Override
        public DataSourceFactory getDataSourceFactory(AppConfig configuration) {
            return configuration.getDataSourceFactory();
        }

        @Override
        public JooqFactory getJooqFactory(AppConfig configuration) {
            return configuration.getJooqFactory();
        }
    });
}
```


This will enable `@Context` injection of jOOQ [Configuration](http://www.jooq.org/javadoc/3.5.0/org/jooq/Configuration.html) and [DSLContext](http://www.jooq.org/javadoc/3.5.0/org/jooq/DSLContext.html) parameters in resource methods:

```java
@GET
@Path("/posts/{id}")
public BlogPost getPost(@QueryParam("id") int postId, @Context DSLContext database) {
    BlogPostRecord post = database
        .selectFrom(POST)
        .where(POST.ID.equal(postId))
        .fetchOne();

    // do stuff
}
```

This will also enable database healthchecks and install exception mappers.

`PostgresSupport` provides a few helpers for aggregating array values in queries. For example:

```java
import static ai.dot.dwtools.jooq.PostgresSupport.arrayAgg;

database
    .select(BLOG_POST.ID, BLOG_POST.BODY, BLOG_POST.CREATED_AT, arrayAgg(POST_TAG.TAG_NAME))
    .from(BLOG_POST)
    .leftOuterJoin(POST_TAG)
    .on(BLOG_POST.ID.equal(POST_TAG.POST_ID))
    .where(BLOG_POST.ID.equal(id.get()))
    .groupBy(BLOG_POST.ID, BLOG_POST.BODY, BLOG_POST.CREATED_AT)
    .fetchOne();
```


Code Generation
---------------

[`dw-jooq`] provides some classes for making generated pojos, DAOs, etc. more convenient to use.

`InstantConverter` can be used to map between `java.sql.Timestamp` and  java.time `Instant` objects.  This is currently the only converter bundled, yet.


Configuration
-------------

[`dw-jooq`] uses the same `DataSourceFactory` for configuring its `DataSource`.

For modifying jOOQ configuration settings, there is `JooqFactory`:

```yaml
jooq:
  # The flavor of SQL to generate. If not specified, it will be inferred from the JDBC connection URL.  (default: null)
  dialect: POSTGRES
  # DEPRECATED: Use `executeLogging` instead (default: no)
  logExecutedSql: yes
  # Whether to include schema names in generated SQL.  (default: yes)
  renderSchema: yes
  # How names should be rendered in generated SQL.  One of QUOTED, AS_IS, LOWER, or UPPER.  (default: QUOTED)
  renderNameStyle: QUOTED
  # How keywords should be rendered in generated SQL.  One of LOWER, UPPER.  (default: UPPER)
  renderKeywordStyle: LOWER
  # Whether generated SQL should be pretty-printed.  (default: no)
  renderFormatted: no
  # How parameters should be represented.  One of INDEXED, NAMED, or INLINE.  (default: INDEXED)
  paramType: INDEXED
  # How statements should be generated; one of PREPARED_STATEMENT or STATIC_STATEMENT.  (default: PREPARED_STATEMENT)
  statementType: PREPARED_STATEMENT
  # Whether internal jOOQ logging should be enabled.  (default: no)
  executeLogging: no
  # Whether optimistic locking should be enabled.  (default: no)
  executeWithOptimisticLocking: no
  # Whether returned records should be 'attached' to the jOOQ context.  (default: yes)
  attachRecords: yes
  # Whether primary-key fields should be updatable.  (default: no)
  updatablePrimaryKeys: no
```
