package org.openmrs.module.patientsearch.api.dao.hibernate.search;

import java.util.Date;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.openmrs.api.db.hibernate.search.LuceneQuery;

/*
 * Lucene query for date fomate 
 */
public class DateLuceneQuery<T> extends LuceneQuery<T> {
	
	public static <T> DateLuceneQuery<T> newQuery(final Class<T> type, final Session session, final Date query,
	        final String field) {
		return new DateLuceneQuery<T>(
		                              type, session) {
			
			@Override
			protected Query prepareQuery() {
				if (query == null) {
					return new MatchAllDocsQuery();
				}
				
				QueryBuilder queryBuilder = getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(type)
				        .get();
				return queryBuilder.range().onField(field).from(query.getTime()).to(query.getTime() + 86400000)
				        .createQuery();
				
			}
		};
	}
	
	public static <T> DateLuceneQuery<T> newQuery(final Class<T> type, final Session session, final Date from,
	        final Date to, final String field) {
		return new DateLuceneQuery<T>(
		                              type, session) {
			
			@Override
			protected Query prepareQuery() {
				if (from == null || to == null) {
					return new MatchAllDocsQuery();
				}
				
				QueryBuilder queryBuilder = getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(type)
				        .get();
				return queryBuilder.range().onField(field).from(from.getTime()).to(to.getTime()).createQuery();
				
			}
		};
	}
	
	public DateLuceneQuery(Class<T> type, Session session) {
		super(type, session);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Query prepareQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
