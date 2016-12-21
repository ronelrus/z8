package org.zenframework.z8.server.db.sql.functions.date;

import java.util.Collection;

import org.zenframework.z8.server.base.table.value.IField;
import org.zenframework.z8.server.db.DatabaseVendor;
import org.zenframework.z8.server.db.FieldType;
import org.zenframework.z8.server.db.sql.FormatOptions;
import org.zenframework.z8.server.db.sql.SqlToken;
import org.zenframework.z8.server.exceptions.db.UnknownDatabaseException;

public class TruncQuarter extends SqlToken {
	private SqlToken date;

	public TruncQuarter(SqlToken date) {
		this.date = date;
	}

	@Override
	public void collectFields(Collection<IField> fields) {
		date.collectFields(fields);
	}

	@Override
	public String format(DatabaseVendor vendor, FormatOptions options, boolean logicalContext) {
		switch(vendor) {
		case Oracle:
			return "Trunc(" + date.format(vendor, options) + ", 'Q')";
		case Postgres:
			return "date_trunc('quarter', " + date.format(vendor, options) + ")";
		case SqlServer:
			return "Convert(datetime, cast(year(" + date.format(vendor, options) + ") as char) +'/'+ cast(((DATEPART ( q , " + date.format(vendor, options) + ")-1)*3+1) as char)+ '/01', 120)";
		default:
			throw new UnknownDatabaseException();
		}
	}

	@Override
	public FieldType type() {
		return date.type();
	}
}
