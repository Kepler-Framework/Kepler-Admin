package com.kepler.admin.generic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kepler.admin.generic.GenericRequest;
import com.kepler.admin.generic.GenericTemplate;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.mongo.impl.MongoUtils;
import com.kepler.service.Service;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * @author KimShen
 *
 */
public class DefaultTemplate implements GenericTemplate {

	private static final String FIELD_DATAS = "datas";

	private static final String FIELD_CATALOG = "catalog";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final Log LOGGER = LogFactory.getLog(DefaultTemplate.class);

	private final MongoConfig config;

	public DefaultTemplate(MongoConfig config) {
		super();
		this.config = config;
	}

	@Override
	public GenericRequest get(Service service, String method) {
		try {
			BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
			builder.add(Dictionary.FIELD_METHOD, method);
			builder.add(Dictionary.FIELD_SERVICE, service.service());
			builder.add(Dictionary.FIELD_VERSION, service.version());
			builder.add(DefaultTemplate.FIELD_CATALOG, service.catalog());
			DBObject request = this.config.collection().findOne(builder.get());
			return request != null ? DefaultTemplate.MAPPER.readValue(MongoUtils.asString(request, DefaultTemplate.FIELD_DATAS), DefaultRequest.class) : null;
		} catch (Exception e) {
			DefaultTemplate.LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void set(GenericRequest request) {
		try {
			BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
			builder.add(Dictionary.FIELD_METHOD, request.getMethod());
			builder.add(Dictionary.FIELD_SERVICE, request.metadata().service());
			builder.add(Dictionary.FIELD_VERSION, request.metadata().version());
			DBObject query = builder.get();
			builder.add(DefaultTemplate.FIELD_CATALOG, request.metadata().catalog());
			builder.add(DefaultTemplate.FIELD_DATAS, DefaultTemplate.MAPPER.writeValueAsString(request));
			DBObject update = builder.get();
			this.config.collection().update(query, update, true, false);
		} catch (Exception e) {
			DefaultTemplate.LOGGER.error(e.getMessage(), e);
		}
	}
}
