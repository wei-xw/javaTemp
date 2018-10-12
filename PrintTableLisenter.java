package com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.bonc.dataplatform.repository.parser.antlr4.oracle.entity.Table;
import com.bonc.dataplatform.repository.parser.antlr4.oracle.listener.SqlParserErrorListener;
import com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlBase.SqlForOracleBaseListener;
import com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlBase.SqlForOracleLexer;
import com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlBase.SqlForOracleParser;
import com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlBase.SqlForOracleParser.SimpleTableContext;
import com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlBase.SqlForOracleParser.TableNameContext;
import com.bonc.dataplatform.repository.parser.antlr4.oracle.tools.TableColumnList;

@SuppressWarnings("deprecation")
public class PrintTableLisenter extends SqlForOracleBaseListener {
	// 只针对一条sql
	Set<Table> directTables = new LinkedHashSet<Table>();
	// List<Table> tableList = new ArrayList<>();
	// List<String> tableNameList = new ArrayList<>();

	public Set<Table> getDirectTables() {
		return directTables;
	}

	public void setDirectTables(Set<Table> directTables) {
		this.directTables = directTables;
	}

	public Set<Table> getSourceTables() {
		return sourceTables;
	}

	public void setSourceTables(Set<Table> sourceTables) {
		this.sourceTables = sourceTables;
	}

	Set<Table> sourceTables = new LinkedHashSet<Table>();
	Set<Table> directTablesNeedCol = new LinkedHashSet<Table>();
	Set<Table> sourceTablesNeedCol = new LinkedHashSet<Table>();
	Set<Table> joinSourceTablesNeedCol = new LinkedHashSet<Table>();
	public TableColumnList map = new TableColumnList("");

	public void exitProg(SqlForOracleParser.ProgContext ctx) {
		List<Table> sourceList = new ArrayList<>();
		List<Table> directList = new ArrayList<>();
		sourceList.addAll(sourceTables);
		directList.addAll(directTables);

		for (int i = 0; i < sourceList.size(); i++) {
			for (int j = 0; j < directList.size(); j++) {
				if (sourceList.get(i).equals(directList.get(j))) {
					sourceTables.remove(sourceList.get(i));
					break;
				}
			}
		}
	}

	public void enterInsertClause(SqlForOracleParser.InsertClauseContext ctx) {
		// -----暂时只处理目标表是simple类型的情况，连接或其他情况后续需要处理-----
		if (ctx.tableSource() instanceof SqlForOracleParser.SimpleTableContext) {
			SqlForOracleParser.SimpleTableContext tableSource = (SimpleTableContext) ctx.tableSource();
			Table table = visitTableName(tableSource.tableName());
			directTables.add(table);
//			if (ctx.columnlist() == null) {
//				directTablesNeedCol.add(table);
//			} else {
//				List<Column> cols = new ArrayList<Column>();
//				for (SqlForOracleParser.ColumnContext col : ctx.columnlist().column()) {
//					Column col1 = new Column();
//					col1.setColumnName(col.getText());
//					col1.setNote(col.getNote());
//					cols.add(col1);
//				}
//				table.setColumnList(cols);
//				map.addTable(table);
//			}
		} else {

		}
	}
/*
	public void exitUnion(SqlForOracleParser.UnionContext ctx) {
		ctx.columnList = ctx.unionQuery().selectQueryBlock1(0).columnList;
	}

	public void exitSelect(SqlForOracleParser.SelectContext ctx) {
		ctx.columnList = ctx.selectQueryBlock().columnList;
	}

	private List<Column> visitFieldExpression(SqlForOracleParser.FieldExpressionContext fieldExpression) {
		List<Column> hs = new ArrayList<Column>();
		Column col = new Column();
		if (fieldExpression.caseExpression() != null)
			hs.addAll(visitCaseExpression(fieldExpression.caseExpression()));
		if (fieldExpression.methodExpression() != null)
			hs.addAll(visitMethodExpression(fieldExpression.methodExpression()));
		if (fieldExpression.fieldExpression() != null) {
			for (SqlForOracleParser.FieldExpressionContext fieldExp : fieldExpression.fieldExpression())
				hs.addAll(visitFieldExpression(fieldExp));
		}
		if (fieldExpression.fieldName() != null
				&& fieldExpression.fieldName() instanceof SqlForOracleParser.IdentifierFieldContext
				&& !fieldExpression.fieldName().getText().contains("$")) {
			SqlForOracleParser.IdentifierFieldContext identifier = (SqlForOracleParser.IdentifierFieldContext) fieldExpression
					.fieldName();
			col.setColumnName(identifier.IDENTIFIER().getText());
			if (identifier.tableName() != null)
				col.setTableOrAlias(identifier.tableName().getText());
			hs.add(col);
		}
		return hs;
	}

	private List<Column> visitMethodExpression(SqlForOracleParser.MethodExpressionContext methodExpressionContext) {
		List<Column> hs = new ArrayList<Column>();
		if (methodExpressionContext.fieldExpression() != null) {
			for (SqlForOracleParser.FieldExpressionContext fieldExp : methodExpressionContext.fieldExpression())
				hs.addAll(visitFieldExpression(fieldExp));
		}
		return hs;
	}

	private List<Column> visitCaseExpression(SqlForOracleParser.CaseExpressionContext caseExpressionContext) {
		List<Column> hs = new ArrayList<Column>();
		if (caseExpressionContext.fieldExpression() != null) {
			for (SqlForOracleParser.FieldExpressionContext fieldExp : caseExpressionContext.fieldExpression())
				hs.addAll(visitFieldExpression(fieldExp));
		}
		if (caseExpressionContext.booleanExpression() != null) {
			for (SqlForOracleParser.BooleanExpressionContext booleanExp : caseExpressionContext.booleanExpression())
				hs.addAll(visitBooleanExpression(booleanExp));
		}
		return hs;
	}

	private List<Column> visitBooleanExpression(SqlForOracleParser.BooleanExpressionContext booleanExpression) {
		List<Column> hs = new ArrayList<Column>();
		if (booleanExpression.fieldExpression() != null) {
			for (SqlForOracleParser.FieldExpressionContext fieldExp : booleanExpression.fieldExpression())
				hs.addAll(visitFieldExpression(fieldExp));
		}
		if (booleanExpression.booleanExpression() != null) {
			for (SqlForOracleParser.BooleanExpressionContext booleanExp : booleanExpression.booleanExpression())
				hs.addAll(visitBooleanExpression(booleanExp));
		}
		return hs;
	}

	public List<Column> getColumnRealList(SqlForOracleParser.SelectQueryBlockContext ctx) {
		List<Column> columnRealList = new ArrayList<Column>();
		for (SqlForOracleParser.ColumnContext columnContext : ctx.selectClause().columnlist().column()) {
			Column col = new Column();
			col.setExp(columnContext.fieldExpression().getText());
			if (columnContext.alias() != null) { // 表达式
				col.setAlias(columnContext.alias().getText());
			}
			columnRealList.add(col);
		}
		return columnRealList;
	}

	private List<Column> visitWhere(SqlForOracleParser.WhereClauseContext whereClause) {
		if (whereClause != null) {
			return visitBooleanExpression(whereClause.booleanExpression());
		}
		return new ArrayList<Column>();
	}

	private List<Column> visitOrderBy(SqlForOracleParser.OrderByClauseContext orderByClause,
			List<Column> columnRealList) {
		// TODO Auto-generated method stub
		List<Column> hs = new ArrayList<Column>();
		List<Column> haveAlias = new ArrayList<>();// 用来存储columnRealList中存在别名情况的字段
		List<Column> hs1 = new ArrayList<Column>();
		if (orderByClause != null) {
			for (SqlForOracleParser.OrderItemContext item : orderByClause.orderList().orderItem()) {
				hs.addAll(visitFieldExpression(item.fieldExpression()));// 将OrderBy节点所有的字段都存储到hs中，包括后面有别名的情况
			}
			hs1.addAll(hs);
			for (Column col : columnRealList) {
				if (!col.getAlias().equals("")) {
					haveAlias.add(col);
				}
			}
			// 遍历hs中所有元素，找到当前元素的列名是否与haveAlias中的名称相同，如果相同，证明OrderBy中存在了使用别名进行排序的情况，将这个元素从hs中去掉
			for (Column col : hs) {
				for (Column col1 : haveAlias) {
					if (col.equalsColumnName(col1.getAlias())) {
						hs1.remove(col);
						break;
					}
				}
			}
		}
		return hs1;
	}

	private List<Column> visitGroupByExpression(GroupByClauseContext groupByClause) {
		List<Column> cols = new ArrayList<Column>();
		if (groupByClause != null) {
			for (SqlForOracleParser.FieldExpressionContext field : groupByClause.fieldExpression()) {
				cols.addAll(visitFieldExpression(field));
			}
		}
		return cols;
	}

	public void exitSelectQueryBlock(SqlForOracleParser.SelectQueryBlockContext ctx) {
		List<Column> columnList = new ArrayList<Column>();
		for (SqlForOracleParser.ColumnContext column : ctx.selectClause().columnlist().column()) {
			if (column.alias() != null) {
				columnList.add(new Column(column.alias().getText()));
			} else if ((column.fieldExpression().fieldName() instanceof SqlForOracleParser.IdentifierFieldContext)
					&& !column.fieldExpression().fieldName().getText().contains("$")) {
				SqlForOracleParser.IdentifierFieldContext identifier = (SqlForOracleParser.IdentifierFieldContext) column
						.fieldExpression().fieldName();
				columnList.add(new Column(identifier.IDENTIFIER().getText()));
			} else if (column.fieldExpression().allFields() != null) {
				if (column.fieldExpression().allFields().tableName() == null) {
					columnList.addAll(ctx.fromClause().tableSource().columnList);
				} else {
					columnList.addAll(ctx.fromClause().tableSource().joinColumns
							.get(column.fieldExpression().allFields().tableName().getText()));
				}
			}
		}
		ctx.columnList = columnList;
		if (ctx.fromClause().tableSource() instanceof SqlForOracleParser.SelectjoinContext) {
			SqlForOracleParser.SelectjoinContext selectJoin = (SqlForOracleParser.SelectjoinContext) ctx.fromClause()
					.tableSource();
			Map<String, Table> aliasTables = new HashMap<String, Table>();
			List<Column> fromColumnList = new ArrayList<Column>();
			for (SqlForOracleParser.TableSourceContext table : selectJoin.tableSource()) {
				if (table instanceof SqlForOracleParser.SimpleTableContext) {
					aliasTables.put(table.getTable().getTableAlias(), table.getTable());
				} else {
					fromColumnList.addAll(table.columnList);
				}
			}
			if (aliasTables.size() > 0) {
				Set<Column> columnSetIndex = new LinkedHashSet<>();
				for (SqlForOracleParser.ColumnContext columnContext : ctx.selectClause().columnlist().column()) {
					// oracle不支持select * from join。。。;*替换为from之后所有的字段。
					columnSetIndex.addAll(visitFieldExpression(columnContext.fieldExpression()));
					if (columnContext.fieldExpression().allFields() != null
							&& columnContext.fieldExpression().allFields().tableName() != null) {
						String tableName = columnContext.fieldExpression().allFields().tableName().getText();
						if (aliasTables.get(tableName) != null) {
							joinSourceTablesNeedCol.add(aliasTables.get(tableName));
							sourceTablesNeedCol.add(aliasTables.get(tableName));
							return;
						}
					}
				}
				for (SqlForOracleParser.SelectActionContext selectAction : ctx.selectAction()) {
					columnSetIndex.addAll(visitWhere(selectAction.whereClause()));
					columnSetIndex.addAll(visitOrderBy(selectAction.orderByClause(), getColumnRealList(ctx)));
					columnSetIndex.addAll(visitGroupByExpression(selectAction.groupByClause()));
				}
				for (SqlForOracleParser.BooleanExpressionContext booleanExpression : selectJoin.booleanExpression()) {
					columnSetIndex.addAll(visitBooleanExpression(booleanExpression));
				}

				boolean isAllTableDotField = true;// 所有的字段都是表名.字段名的形式。
				for (Column col : columnSetIndex) {
					if (col.getTableOrAlias().equals("")) {
						for (Column col1 : fromColumnList) {
							if (col1.equalsColumnName(col.getColumnName()))
								col.setTableOrAlias(col1.getTableOrAlias());
						}
						if (col.getTableOrAlias().equals("")) {
							isAllTableDotField = false;
						}
					}
				}
				if (isAllTableDotField) {
					for (SqlForOracleParser.TableSourceContext table : selectJoin.tableSource()) {
						if (table instanceof SqlForOracleParser.SimpleTableContext
								&& map.get(table.getTable()) == null) {// 子查询不管，只往简单表中填字段
							table.getTable().setColumnList(new ArrayList<Column>());
							map.addTable(table.getTable());
						}
					}
					for (Column col : columnSetIndex) {
						if (aliasTables.get(col.getTableOrAlias()) != null) {
							List<Column> tmp = map.get(aliasTables.get(col.getTableOrAlias())).getColumnList();
							tmp.add(col);
						}
					}
				} else {
					for (Table tab : aliasTables.values()) {
						joinSourceTablesNeedCol.add(tab);
						sourceTablesNeedCol.add(tab);
					}

				}
			}
		} else if (ctx.fromClause().tableSource() instanceof SqlForOracleParser.SimpleTableContext) {
			boolean isNeed = false;
			for (SqlForOracleParser.ColumnContext columnContext : ctx.selectClause().columnlist().column()) {
				if (columnContext.fieldExpression().allFields() != null) {
					isNeed = true;
					break;
				}
			}
			Table tab = ctx.fromClause().tableSource().getTable();
			List<Column> cols = new ArrayList<Column>();
			if (map.get(tab) != null) {
				cols = map.get(tab).getColumnList();
				isNeed = false;
			}
			if (isNeed) {
				sourceTablesNeedCol.add(ctx.fromClause().tableSource().getTable());
			} else {
				Set<Column> columnSetIndex = new LinkedHashSet<>();
				for (SqlForOracleParser.ColumnContext columnContext : ctx.selectClause().columnlist().column()) {
					columnSetIndex.addAll(visitFieldExpression(columnContext.fieldExpression()));
				}
				for (SqlForOracleParser.SelectActionContext selectAction : ctx.selectAction()) {
					columnSetIndex.addAll(visitWhere(selectAction.whereClause()));
					columnSetIndex.addAll(visitOrderBy(selectAction.orderByClause(), getColumnRealList(ctx)));
					columnSetIndex.addAll(visitGroupByExpression(selectAction.groupByClause()));
				}
				columnSetIndex.addAll(cols);
				List<Column> list = new ArrayList<>();
				for (Column col : columnSetIndex) {
					col.setTableOrAlias(tab.getDatabase() + "." + tab.getTableName());
					list.add(col);
				}
				tab.setColumnList(list);
				map.addTable(tab);
			}
		}
	}

	public void exitSelectQueryBlock1(SqlForOracleParser.SelectQueryBlock1Context ctx) {
		List<Column> columnList = new ArrayList<Column>();
		for (SqlForOracleParser.ColumnContext column : ctx.selectClause().columnlist().column()) {
			if (column.alias() != null) {
				columnList.add(new Column(column.alias().getText()));
			} else if ((column.fieldExpression().fieldName() instanceof SqlForOracleParser.IdentifierFieldContext)
					&& !column.fieldExpression().fieldName().getText().contains("$")) {
				SqlForOracleParser.IdentifierFieldContext identifier = (SqlForOracleParser.IdentifierFieldContext) column
						.fieldExpression().fieldName();
				columnList.add(new Column(identifier.IDENTIFIER().getText()));
			} else if (column.fieldExpression().allFields() != null) {
				if (column.fieldExpression().allFields().tableName() == null) {
					columnList.addAll(ctx.fromClause().tableSource().columnList);
				} else {
					columnList.addAll(ctx.fromClause().tableSource().joinColumns
							.get(column.fieldExpression().allFields().tableName().getText()));
				}
			}
		}
		ctx.columnList = columnList;
		if (ctx.fromClause().tableSource() instanceof SqlForOracleParser.SelectjoinContext) {
			SqlForOracleParser.SelectjoinContext selectJoin = (SqlForOracleParser.SelectjoinContext) ctx.fromClause()
					.tableSource();
			Map<String, Table> aliasTables = new HashMap<String, Table>();
			List<Column> fromColumnList = new ArrayList<Column>();
			for (SqlForOracleParser.TableSourceContext table : selectJoin.tableSource()) {
				if (table instanceof SqlForOracleParser.SimpleTableContext) {
					aliasTables.put(table.getTable().getTableAlias(), table.getTable());
				} else {
					fromColumnList.addAll(table.columnList);
				}
			}
			if (aliasTables.size() > 0) {
				Set<Column> columnSetIndex = new LinkedHashSet<>();
				for (SqlForOracleParser.ColumnContext columnContext : ctx.selectClause().columnlist().column()) {
					// oracle不支持select * from join。。。;*替换为from之后所有的字段。
					columnSetIndex.addAll(visitFieldExpression(columnContext.fieldExpression()));
					if (columnContext.fieldExpression().allFields() != null
							&& columnContext.fieldExpression().allFields().tableName() != null) {
						String tableName = columnContext.fieldExpression().allFields().tableName().getText();
						if (aliasTables.get(tableName) != null) {
							joinSourceTablesNeedCol.add(aliasTables.get(tableName));
							sourceTablesNeedCol.add(aliasTables.get(tableName));
							return;
						}
					}
				}
				for (SqlForOracleParser.SelectAction1Context selectAction : ctx.selectAction1()) {
					columnSetIndex.addAll(visitWhere(selectAction.whereClause()));
					columnSetIndex.addAll(visitGroupByExpression(selectAction.groupByClause()));
				}
				for (SqlForOracleParser.BooleanExpressionContext booleanExpression : selectJoin.booleanExpression()) {
					columnSetIndex.addAll(visitBooleanExpression(booleanExpression));
				}

				boolean isAllTableDotField = true;// 所有的字段都是表名.字段名的形式。
				for (Column col : columnSetIndex) {
					if (col.getTableOrAlias().equals("")) {
						for (Column col1 : fromColumnList) {
							if (col1.equalsColumnName(col.getColumnName()))
								col.setTableOrAlias(col1.getTableOrAlias());
						}
						if (col.getTableOrAlias().equals("")) {
							isAllTableDotField = false;
						}
					}
				}
				if (isAllTableDotField) {
					for (SqlForOracleParser.TableSourceContext table : selectJoin.tableSource()) {
						if (table instanceof SqlForOracleParser.SimpleTableContext) {// 子查询不管，只往简单表中填字段
							table.getTable().setColumnList(new ArrayList<Column>());
							map.addTable(table.getTable());
						}
					}
					for (Column col : columnSetIndex) {
						if (aliasTables.get(col.getTableOrAlias()) != null) {
							List<Column> tmp = map.get(aliasTables.get(col.getTableOrAlias())).getColumnList();
							tmp.add(col);
						}
					}
				} else {
					for (Table tab : aliasTables.values()) {
						sourceTablesNeedCol.add(tab);
						joinSourceTablesNeedCol.add(tab);
					}
				}
			}
		} else if (ctx.fromClause().tableSource() instanceof SqlForOracleParser.SimpleTableContext) {
			boolean isNeed = false;
			for (SqlForOracleParser.ColumnContext columnContext : ctx.selectClause().columnlist().column()) {
				if (columnContext.fieldExpression().allFields() != null) {
					isNeed = true;
					break;
				}
			}
			if (isNeed) {
				sourceTablesNeedCol.add(ctx.fromClause().tableSource().getTable());
			} else {
				Set<Column> columnSetIndex = new LinkedHashSet<>();
				for (SqlForOracleParser.ColumnContext columnContext : ctx.selectClause().columnlist().column()) {
					columnSetIndex.addAll(visitFieldExpression(columnContext.fieldExpression()));
				}
				for (SqlForOracleParser.SelectAction1Context selectAction : ctx.selectAction1()) {
					columnSetIndex.addAll(visitWhere(selectAction.whereClause()));
					columnSetIndex.addAll(visitGroupByExpression(selectAction.groupByClause()));
				}
				Table tab = ctx.fromClause().tableSource().getTable();
				List<Column> list = new ArrayList<>();
				for (Column col : columnSetIndex) {
					col.setTableOrAlias(tab.getDatabase() + "." + tab.getTableName());
					list.add(col);
				}
				tab.setColumnList(list);
				map.addTable(tab);
			}
		}
	}

	public void exitSelectjoin(SqlForOracleParser.SelectjoinContext ctx) {
		for (SqlForOracleParser.TableSourceContext table : ctx.tableSource()) {
			ctx.joinColumns.put(table.alias, table.columnList);
		}
	}

	public void exitSubSelectQuery(SqlForOracleParser.SubSelectQueryContext ctx) {
		String alias;
		if (ctx.alias() != null) {
			alias = ctx.alias().getText();
		} else {
			alias = "";
		}
		for (Column col : ctx.selectStatement().columnList) {
			col.setTableOrAlias(alias);
		}
		ctx.columnList = ctx.selectStatement().columnList;
	}
	
	 * (non-Javadoc)
	 * @see com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlBase.SqlForOracleBaseListener#exitSimpleTable(com.bonc.dataplatform.repository.parser.antlr4.oracle.procedure.sqlBase.SqlForOracleParser.SimpleTableContext)
	 */

	public void exitSimpleTable(SqlForOracleParser.SimpleTableContext ctx) {
		Table table = visitTableName(ctx.tableName());
		sourceTables.add(table);
		if (ctx.alias() != null)
			table.setTableAlias(ctx.alias().getText());
		ctx.setTable(table);
	}

	private Table visitTableName(TableNameContext tableName) {
		if (tableName.database() != null) {
			Table table = new Table((tableName.getText().substring(tableName.database().getText().length() + 1)));
			table.setTableAlias(table.getTableName());
			table.setDatabase(tableName.database().getText());
			return table;
		} else {
			Table table = new Table(tableName.getText());
			table.setTableAlias(table.getTableName());
			return table;
		}
	}

	public static void main(String[] args) {
		File file = new File("C:\\Users\\BONC\\Desktop\\222.txt");
		String str = "";

		BufferedReader br;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(file));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str = sb.toString();

		SqlForOracleLexer lexer = new SqlForOracleLexer(new ANTLRInputStream(str));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SqlForOracleParser parser = new SqlForOracleParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new SqlParserErrorListener());
		ParseTree tree = parser.prog();
		ParseTreeWalker walker = new ParseTreeWalker();

		PrintTableLisenter listen = new PrintTableLisenter();
		walker.walk(listen, tree);
		System.out.println(listen.sourceTables);
	}
}
