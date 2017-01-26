package org.zenframework.z8.server.base.form;

import org.zenframework.z8.server.base.query.Query;
import org.zenframework.z8.server.base.table.TreeTable;
import org.zenframework.z8.server.base.table.value.Field;
import org.zenframework.z8.server.base.table.value.Link;
import org.zenframework.z8.server.json.Json;
import org.zenframework.z8.server.json.JsonWriter;
import org.zenframework.z8.server.runtime.IObject;
import org.zenframework.z8.server.runtime.RCollection;
import org.zenframework.z8.server.types.bool;
import org.zenframework.z8.server.types.integer;

public class Listbox extends Control {
	public static class CLASS<T extends Listbox> extends Control.CLASS<T> {
		public CLASS(IObject container) {
			super(container);
			setJavaClass(Listbox.class);
		}

		@Override
		public Object newObject(IObject container) {
			return new Listbox(container);
		}
	}

	public integer height;

	public Query.CLASS<? extends Query> query = null;
	public Link.CLASS<? extends Link> link = null;
	public RCollection<Field.CLASS<? extends Field>> columns = new RCollection<Field.CLASS<? extends Field>>();
	public RCollection<Field.CLASS<? extends Field>> sortFields = new RCollection<Field.CLASS<? extends Field>>();

	public Listbox(IObject container) {
		super(container);
		this.editable = bool.True;
	}

	@Override
	public void writeMeta(JsonWriter writer, Query requestQuery, Query context) {
		if(this.query == null)
			throw new RuntimeException("Listbox.query is null : displayName: '"  + displayName() + "'");

		if(link == null && dependency == null)
			throw new RuntimeException("Both Listbox.link and Control.dependency are null: '"  + displayName() + "'");

		String requestId = requestQuery.classId();
		Query query = this.query.get();

		super.writeMeta(writer, query, context);

		writer.writeProperty(Json.isListbox, true);
		writer.writeProperty(Json.header, displayName());
		writer.writeProperty(Json.icon, icon());

		writer.writeProperty(Json.height, height, new integer(4));

		writer.startObject(Json.query);

		writer.writeProperty(Json.id, requestId);
		writer.writeProperty(Json.name, query.id());
		writer.writeProperty(Json.primaryKey, query.primaryKey().id());
		writer.writeProperty(Json.lockKey, query.lockKey().id());

		Field parentKey = query.parentKey();
		if(parentKey != null)
			writer.writeProperty(Json.parentKey, parentKey.id());

		writer.writeProperty(Json.totals, query.totals);
		writer.writeProperty(Json.text, query.displayName());

		writer.writeControls(Json.fields, query.formFields(), query, context);
		writer.writeControls(Json.columns, columns.isEmpty() ? query.columns() : CLASS.asList(columns), query, context);
		writer.writeSort(sortFields.isEmpty() ? query.sortFields() : CLASS.asList(sortFields));

		if(link != null) {
			writer.startObject(Json.link);

			writer.writeProperty(Json.id, link.id());

			if(link.get().query().instanceOf(TreeTable.class)) {
				writer.startArray(Json.parentKeys);
				for(Field parent : link.get().getQuery().parentKeys())
					writer.write(parent.id());
				writer.finishArray();
			}

			writer.finishObject();
		}

		writer.finishObject();
	}
}
