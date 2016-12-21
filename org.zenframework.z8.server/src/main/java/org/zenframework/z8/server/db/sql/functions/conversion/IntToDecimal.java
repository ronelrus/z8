package org.zenframework.z8.server.db.sql.functions.conversion;

import java.util.Collection;

import org.zenframework.z8.server.base.table.value.IField;
import org.zenframework.z8.server.db.DatabaseVendor;
import org.zenframework.z8.server.db.FieldType;
import org.zenframework.z8.server.db.sql.FormatOptions;
import org.zenframework.z8.server.db.sql.SqlToken;
import org.zenframework.z8.server.exceptions.db.UnknownDatabaseException;

public class IntToDecimal extends SqlToken {
	private SqlToken token;

	public IntToDecimal(SqlToken token) {
		this.token = token;
	}

	@Override
	public void collectFields(Collection<IField> fields) {
		token.collectFields(fields);
	}

	@Override
	public String format(DatabaseVendor vendor, FormatOptions options, boolean logicalContext) {
		switch(vendor) {
		case Oracle:
			return token.format(vendor, options);
		case SqlServer:
		case Postgres:
			return new ToDecimal(token).format(vendor, options);
		default:
			throw new UnknownDatabaseException();
		}
	}

	@Override
	public FieldType type() {
		return FieldType.Decimal;
	}
}
