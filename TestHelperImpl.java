package jp.co.kobelcosys.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.kofraj.service.base.IService;
import jp.kofraj.util.FileUtility;
import jp.kofraj.vo.base.BaseVO;
import jp.kofraj.vo.base.PagingVO;
import jp.kofraj.yodoko.common.ChkMsgManager;
import jp.kofraj.yodoko.common.InfoUtil;
import jp.kofraj.yodoko.common.vo.CommonInfo;
import jp.kofraj.yodoko.common.vo.LogonInfo;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;

/**
 * テストヘルプです。
 *
 * @since 1.0
 * @version 1.0
 * @author CHENP
 */
public class TestHelperImpl implements TestHelper {

	/**
	 * アプリケーションコンテキスト
	 */
	private ApplicationContext context = null;

	/**
	 * サービス
	 */
	private IService service = null;

	/**
	 * DB接続
	 */
	private Connection conn = null;

	/**
	 * テストタイプ（true:単体テスト false:回帰テスト）
	 */
	private boolean processType = false;

	/**
	 * アクションタイプ（0:doEdit 1:validate 2:validateAndEdit）
	 */
	private int actionType = DO_EDIT;

	/**
	 * テーブル名前
	 */
	private String[] tableNames = null;

	/**
	 * ファイルパス
	 */
	private String filePath = "./src/test/resources/jp/kofraj/";

	/**
	 * TPファイルパス
	 */
	private String tpFilePath = "";

	/**
	 * TEファイルパス
	 */
	private String teFilePath = "";

	/**
	 * TRファイルパス
	 */
	private String trFilePath = "";

	/**
	 * プロダクトID
	 */
	private String productId = "";

	/**
	 * LogonInfoパス
	 */
	private String logonInfoPath = "";

	/**
	 * CommonInfoパス
	 */
	private String commonInfoPath = "";

	/**
	 * フォントリスト
	 */
	private Map<String, List<XSSFCellStyle>> cellStyleMap = new HashMap<String, List<XSSFCellStyle>>();

	// /**
	// * プロダクト名前
	// */
	// private String productName = "";

	/**
	 * テンプレートファイルパス
	 */
	private String templateFilePath = "./src/test/resources/jp/kofraj/producttest/template/";

	/**
	 * TEテンプレートファイル名前
	 */
	private static String TE_TEMPLATE_FILE_NAME = "EvidenceFormat.xlsx";

	/**
	 * TRテンプレートファイル名前
	 */
	private static String TR_TEMPLATE_FILE_NAME = "RegressionFormat.xlsx";

	// /**
	// * TPシート「画面入力データ」
	// */
	// private static String TP_SHEET_NAME_1 = "画面入力データ";

	/**
	 * TPシート「処理前VO」
	 */
	private static String TP_SHEET_NAME_2 = "処理前DB";

	/**
	 * TPシート「処理前VO」
	 */
	private static String TP_SHEET_NAME_3 = "処理前VO";

	/**
	 * TPシート「共通情報データ」
	 */
	private static String TP_SHEET_NAME_4 = "共通情報データ";

	// /**
	// * TEシート「画面キャプチャ」
	// */
	// private static String TE_SHEET_NAME_1 = "画面キャプチャ";

	/**
	 * TEシート「処理前DB」
	 */
	private static String TE_SHEET_NAME_2 = "処理前DB";

	/**
	 * TEシート「処理後DB」
	 */
	private static String TE_SHEET_NAME_3 = "処理後DB";

	/**
	 * TEシート「処理前VO」
	 */
	private static String TE_SHEET_NAME_4 = "処理前VO";

	/**
	 * TEシート「差分DB」
	 */
	private static String TE_SHEET_NAME_5 = "差分DB";

	/**
	 * TEシート「処理後VO」
	 */
	private static String TE_SHEET_NAME_6 = "処理後VO";

	/**
	 * TEシート「差分VO」
	 */
	private static String TE_SHEET_NAME_7 = "差分VO";

	/**
	 * TEシート「共通情報データ」
	 */
	private static String TE_SHEET_NAME_8 = "共通情報データ";

	// /**
	// * TEシート「ServiceIOs」
	// */
	// private static String TE_SHEET_NAME_9 = "ServiceIOs";
	//
	// /**
	// * TEシート「備考」
	// */
	// private static String TE_SHEET_NAME_10 = "備考";

	/**
	 * TRシート「処理前DB」
	 */
	private static String TR_SHEET_NAME_1 = "処理前DB";

	/**
	 * TRシート「処理前VO」
	 */
	private static String TR_SHEET_NAME_2 = "処理前VO";

	/**
	 * TRシート「共通情報データ」
	 */
	private static String TR_SHEET_NAME_3 = "共通情報データ";

	/**
	 * TRシート「期待DB」
	 */
	private static String TR_SHEET_NAME_4 = "期待DB";

	/**
	 * TRシート「期待VO」
	 */
	private static String TR_SHEET_NAME_5 = "期待VO";

	/**
	 * アクションタイプ「0」:doEdit
	 */
	public static final int DO_EDIT = 0;

	/**
	 * アクションタイプ「1」:validate
	 */
	public static final int DO_VALIDATE = 1;

	/**
	 * アクションタイプ「2」:validateAndEdit
	 */
	public static final int DO_VALIDATE_AND_EDIT = 2;

	/**
	 * 初期化です。
	 *
	 * @param context
	 *            　アプリケーションコンテキスト
	 * @param tableNames
	 *            使用されたテーブル名前の配列
	 * @param processType
	 *            true:単体テスト false:回帰テスト
	 * @throws Exception
	 *             　例外発生時
	 */
	public TestHelperImpl(ApplicationContext context, String[] tableNames)
			throws Exception {
		Class.forName("org.postgresql.Driver").newInstance();
		// データソースの取得
		BasicDataSource datasource = context.getBean("dataSource",
				BasicDataSource.class);
		// DB接続
		conn = DriverManager.getConnection(datasource.getUrl(),
				datasource.getUsername(), datasource.getPassword());
		// アプリケーションコンテキスト
		this.context = context;
		// 使用されたテーブル名前の配列
		this.tableNames = tableNames;

		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/test.properties");
		prop.load(in);
		// テストタイプ
		this.processType = Boolean.parseBoolean(prop.getProperty("test.type")
				.trim());
	}

	/**
	 * JUNITテスト用方法。
	 *
	 * @param pageId
	 *            ページID
	 * @param serviceBeanId
	 *            サービスのビーンID
	 * @param caseNo
	 *            テストのケースNo
	 * @param actionType
	 *            0:doEdit 1:validate 2:validateAndEdit
	 * @throws Exception
	 *             　例外発生時
	 */
	public void runProductTest(String pageId, String serviceBeanId,
			String caseNo, int actionType) throws Exception {
		// エラークリア
		ChkMsgManager.clearChkResultList();

		// テストされるサビースの取得
		service = context.getBean(serviceBeanId, IService.class);

		// getProductIdAndName(pageId);
		// プロダクトID取得
		this.productId = pageId;

		// TP、TE、TRのファイルパスの取得
		tpFilePath = filePath + productId.toLowerCase() + "/input/TP_"
				+ pageId.toUpperCase() + "(" + caseNo + ").xlsx";
		teFilePath = filePath + productId.toLowerCase() + "/evidence/TE_"
				+ pageId.toUpperCase() + "(" + caseNo + ").xlsx";
		trFilePath = filePath + productId.toLowerCase() + "/regression/TR_"
				+ pageId.toUpperCase() + "(" + caseNo + ").xlsx";

		// アクションタイプ
		this.actionType = actionType;

		// シーケンスを再設定
		setSeqNo();

		// 単体テストの場合
		if (processType) {
			// TEとTRファイル存在チェックを行う
			pathCheck(teFilePath);
			pathCheck(trFilePath);

			// テンプレートファイルをコピーして、TEとTRファイルを作成する。
			FileUtility.copy(templateFilePath + TE_TEMPLATE_FILE_NAME,
					teFilePath);
			FileUtility.copy(templateFilePath + TR_TEMPLATE_FILE_NAME,
					trFilePath);

			// 単体テスト
			doUnitTest();
		} else {
			// 回帰テスト
			doRegressTest();
		}

		cellStyleMap = new HashMap<String, List<XSSFCellStyle>>();
	}

	/**
	 * シーケンスを再設定
	 *
	 * @throws SQLException
	 */
	private void setSeqNo() throws SQLException {

		String seqNoTable[] = { "tbl_t_coli_rireki" };

		Statement stmt = conn.createStatement();

		for (String tableName : seqNoTable) {
			// テーブルデータの削除
			stmt.execute("select setval('" + tableName + "_seq_no', 100)");
		}
	}

	/**
	 * 単体テストの行う。
	 *
	 * @throws Exception
	 *             　例外発生時
	 */
	private void doUnitTest() throws Exception {
		// 共通情報データの取得
		getCommonInfoData();
		// 事前DBデータの挿入
		insertData(tpFilePath, true);
		// TEとTRデータの作成
		createTeAndTr();
	}

	/**
	 * 回帰テストの行う。
	 *
	 * @throws Exception
	 *             　例外発生時
	 */
	private void doRegressTest() throws Exception {
		// 共通情報データの取得
		getCommonInfoData();
		// 事前DBデータの挿入
		insertData(trFilePath, false);
		// テスト方法の行う
		doMethodByTr();
		// TR期待DBデータと実行後データの比較の行う
		compareTrDbResultData();
	}

	/**
	 * TPとTRのシート「共通情報データ」データの取得
	 *
	 * @throws Exception
	 *             　例外発生時
	 */
	private void getCommonInfoData() throws Exception {

		String filePath = "";
		// 単体テストの場合
		if (processType) {
			filePath = tpFilePath;
		} else {
			// 回帰テストの場合

			filePath = trFilePath;
		}

		// TE、TRファイルの取得
		InputStream io = new FileInputStream(filePath);
		XSSFWorkbook wb = new XSSFWorkbook(io);

		// シート「共通情報データ」の取得
		XSSFSheet sheet;
		if (processType) {
			sheet = wb.getSheet(TP_SHEET_NAME_4);
		} else {
			sheet = wb.getSheet(TR_SHEET_NAME_3);
		}

		int rows = sheet.getLastRowNum();

		for (int i = 6; i < rows; i++) {
			// 行の取得
			XSSFRow row = sheet.getRow(i);

			// 行がnullではない、一番のセルがnullではない場合
			if ((row != null) && (row.getCell(0) != null)) {

				// 最後行の場合
				row.getCell(0).setCellType(XSSFCell.CELL_TYPE_STRING);
				if ("END".equals(row.getCell(0).getStringCellValue())) {
					break;
				}

				// 一番のセルがVOの場合
				if ("VO".equals(row.getCell(0).toString())) {
					// VOのクラスパスの取得
					String infoClassName = row.getCell(1).toString();

					// VOが共通情報(LogonInfo)の場合
					if (infoClassName.contains("LogonInfo")) {
						logonInfoPath = infoClassName;

						Class<?> c = LogonInfo.class;
						Object cInstance = c.newInstance();

						// 共通情報(LogonInfo)のキー行の取得
						XSSFRow logonInfoKeyRow = sheet.getRow(i + 1);
						// 共通情報(LogonInfo)の値行の取得
						XSSFRow logonInfoValueRow = sheet.getRow(i + 3);

						// 列数の取得
						int columns = logonInfoKeyRow.getLastCellNum();

						for (int j = 0; j < columns - 1; j++) {
							// キーの取得
							String key = logonInfoKeyRow.getCell(j + 1)
									.getStringCellValue();
							// 値の取得
							String value = logonInfoValueRow.getCell(j + 1)
									.getStringCellValue();
							value = StringUtils.isBlank(value) ? "" : value;

							// セット方法名の取得
							String methodName = "set"
									+ key.substring(0, 1).toUpperCase()
									+ key.substring(1);

							// キーがロールの場合
							if ("roleCodes".equals(key)) {
								// ロールのセットを行う
								Class<?> type = getCurField(c, "roleCodes")
										.getType();
								String[] values = new String[] { value };
								Method method = c.getMethod(methodName, type);
								method.invoke(cInstance,
										new Object[] { values });
							} else {
								// 別のデータのセットを行う
								Method method = c.getMethod(methodName,
										String.class);
								method.invoke(cInstance, value);
							}
						}

						// 共通情報(LogonInfo)データのセット
						InfoUtil.setLogonInfo((LogonInfo) cInstance);
					}

					// VOが共通情報(CommonInfo)の場合
					if (infoClassName.contains("CommonInfo")) {
						commonInfoPath = infoClassName;

						// 共通情報(CommonInfo)対象の作成
						Class<?> c = CommonInfo.class;
						Object cInstance = c.newInstance();

						// 共通情報(CommonInfo)のキー行の取得
						XSSFRow commonInfoKeyRow = sheet.getRow(i + 1);
						// 共通情報(CommonInfo)の値行の取得
						XSSFRow commonInfoValueRow = sheet.getRow(i + 3);

						// 列数の取得
						int columns = commonInfoKeyRow.getLastCellNum();

						for (int j = 0; j < columns - 1; j++) {
							// キーの取得
							String key = commonInfoKeyRow.getCell(j + 1)
									.getStringCellValue();
							// 値の取得
							String value = commonInfoValueRow.getCell(j + 1)
									.getStringCellValue();
							value = StringUtils.isBlank(value) ? "" : value;

							// セット方法名の取得
							String methodName = "set"
									+ key.substring(0, 1).toUpperCase()
									+ key.substring(1);

							// 別のデータのセットを行う
							Method method = c.getMethod(methodName,
									String.class);
							method.invoke(cInstance, value);
						}

						// 共通情報(CommonInfo)データのセット
						InfoUtil.setCommonInfo((CommonInfo) cInstance);
					}
				}
			}
		}
	}

	/**
	 * 事前DBデータの削除を行う。
	 *
	 * @param rows
	 *            行数
	 * @param sheet
	 *            シート
	 * @throws Exception
	 *             　例外発生時
	 */
	private void deleteData(int rows, XSSFSheet sheet) throws Exception {

		List<String> resultList = new ArrayList<String>();
		for (int i = 5; i < rows; i++) {
			// 行の取得
			XSSFRow row = sheet.getRow(i);
			// ブランク行ではない場合
			if ((row != null) && (row.getCell(0) != null)) {
				row.getCell(0).setCellType(XSSFCell.CELL_TYPE_STRING);
				if ("END".equals(row.getCell(0).getStringCellValue())) {
					break;
				}

				// 第一列がTABLEの場合
				if ("TABLE".equals(row.getCell(0).toString())) {

					// テーブル名前の取得
					String tName = row.getCell(1).getStringCellValue();
					resultList.add(tName);
				}
			}
		}

		for (int k = resultList.size() - 1; k >= 0; k--) {
			Statement stmt = conn.createStatement();

			String tName = resultList.get(k);
			// テーブルデータの削除
			stmt.execute("DELETE FROM " + tName);
		}

	}

	/**
	 * 事前DBデータの挿入を行う。
	 *
	 * @param filePath
	 *            ファイルパス
	 * @throws Exception
	 *             　例外発生時
	 */
	private void insertData(String filePath, boolean processType)
			throws Exception {
		InputStream io = new FileInputStream(filePath);
		XSSFWorkbook wb = new XSSFWorkbook(io);

		// シート「処理前DB」の取得
		XSSFSheet sheet;
		if (processType) {
			sheet = wb.getSheet(TP_SHEET_NAME_2);
		} else {
			sheet = wb.getSheet(TR_SHEET_NAME_1);
		}

		// ファイルシート「事前データ」の行数
		int rows = sheet.getLastRowNum();

		// 2016/01/20 glad chenq add start
		deleteData(rows, sheet);
		// 2016/01/20 glad chenq add end

		String tableName = "";
		String sql = "";
		int columnCount = 0;
		List<String> keyList = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 5; i < rows; i++) {
			// 行の取得
			XSSFRow row = sheet.getRow(i);

			// ブランク行ではない場合
			if ((row != null) && (row.getCell(0) != null)) {
				row.getCell(0).setCellType(XSSFCell.CELL_TYPE_STRING);
				if ("END".equals(row.getCell(0).getStringCellValue())) {
					break;
				}

				Statement stmt = conn.createStatement();

				// 第一列がTABLEの場合
				if ("TABLE".equals(row.getCell(0).toString())) {
					// テーブル名前の取得
					tableName = row.getCell(1).getStringCellValue();

					// テーブルデータの削除
					stmt.execute("DELETE FROM " + tableName);
					PreparedStatement pstmt = conn
							.prepareStatement("SELECT * FROM " + tableName);
					ResultSet rs = pstmt.executeQuery();

					// テーブルの列と列のタイプを取得する。
					ResultSetMetaData rsmd = rs.getMetaData();
					if (rsmd != null) {
						int count = rsmd.getColumnCount();

						for (int j = 1; j <= count; j++) {
							map.put(rsmd.getColumnName(j).toUpperCase(),
									rsmd.getColumnTypeName(j));
						}
					}

					// 列カウント
					columnCount = 0;

					sql = "";
					sql += "INSERT INTO " + tableName + " ";
				} else {
					// 第二列がCOLUMNの場合
					if ("COLUMN".equals(row.getCell(0).toString())) {
						// 列数の取得
						int columns = row.getLastCellNum();

						sql += "( " + row.getCell(1).getStringCellValue();

						// テーブルの列リストの取得
						keyList.add(0, row.getCell(1).getStringCellValue());

						// 列カウント+1
						columnCount++;

						for (int j = 1; j < columns - 1; j++) {

							if ((row.getCell(j + 1) == null)
									|| (row.getCell(j + 1).getStringCellValue() == null)
									|| ("".equals(row.getCell(j + 1)
											.getStringCellValue().trim()))) {
								break;
							}

							String key = row.getCell(j + 1)
									.getStringCellValue();

							// 列カウント+1
							columnCount++;

							sql += ", " + key;

							// テーブルの列リストの取得
							keyList.add(j, row.getCell(j + 1)
									.getStringCellValue());
						}
						sql += " )";
					} else {

						// 第二列が項目名ではない場合
						if (!"項目名".equals(row.getCell(0).toString())) {
							String tempSql = sql;

							String value = row.getCell(1).getStringCellValue();

							// 列のタイプが日付の場合
							if ("numeric".equals(map.get(keyList.get(0)
									.toUpperCase()))) {
								value = StringUtils.isBlank(value) ? null
										: value;
								tempSql += " VALUES ( " + value + "";
							} else {
								if (StringUtils.isBlank(value)) {
									tempSql += ", null";
								} else if ("''".equals(value)) {
									tempSql += " VALUES ( ''";
								} else {
									tempSql += " VALUES ( '" + value + "'";
								}
							}

							for (int k = 1; k < columnCount; k++) {
								row.getCell(k + 1).setCellType(
										XSSFCell.CELL_TYPE_STRING);
								value = row.getCell(k + 1).getStringCellValue();

								// 列のタイプが日付の場合
								if ("numeric".equals(map.get(keyList.get(k)
										.toUpperCase()))) {
									value = StringUtils.isBlank(value) ? null
											: value;
									tempSql += ", " + value + "";
								} else {
									if (StringUtils.isBlank(value)) {
										tempSql += ", null";
									} else if ("''".equals(value)) {
										tempSql += ", ''";
									} else {
										tempSql += ", '" + value + "'";
									}
								}
							}
							tempSql += " )";

							// テーブルにデータの挿入を行う。
							stmt.execute(tempSql);
						}

					}
				}
			} else {
				// テーブル名前のクリア
				tableName = "";
				// テーブルキーの名前とタイプのクリア
				map = new HashMap<String, Object>();
				// テーブルのキーリストのクリア
				keyList = new ArrayList<String>();
			}
		}
	}

	/**
	 * TEとTRファイルを作成する。
	 *
	 * @throws Exception
	 *             例外発生時
	 */
	private void createTeAndTr() throws Exception {
		XSSFWorkbook wbTe = new XSSFWorkbook(new FileInputStream(teFilePath));
		XSSFWorkbook wbTr = new XSSFWorkbook(new FileInputStream(trFilePath));

		createCellStyle(wbTe, wbTr);

		// // プロダクトIDの切り替え。
		// changeProductId(wbTe, wbTr);

		// TEファイルのシート「共通情報データ」を作成する
		createSheetCommonInfoData(wbTe, TE_SHEET_NAME_8, teFilePath);
		// TRファイルのシート「共通情報データ」を作成する
		createSheetCommonInfoData(wbTr, TR_SHEET_NAME_3, trFilePath);

		// TEのシート「処理前DB」とTRのシート「事前データ」を作成する
		createSheetForInAndOutDB(wbTe, wbTr, true);
		// テスト方法を行う。
		doMethodForCreateTrAndTe(wbTe, wbTr);
		// TEのシート「処理前DB」とTRのシート「事前データ」を作成する
		createSheetForInAndOutDB(wbTe, wbTr, false);

		// TEのシート「差分DB」を作成する
		createDifferentDBSheet(wbTe);
		// // TEのシート「差分VO」を作成する
		createDifferentVoSheet(wbTe);

		// TEファイルの保存
		FileOutputStream outTe = null;
		try {
			outTe = new FileOutputStream(teFilePath);
			wbTe.write(outTe);
		} finally {
			outTe.close();
		}

		// TRファイルの保存
		FileOutputStream outTr = null;
		try {
			outTr = new FileOutputStream(trFilePath);
			wbTr.write(outTr);
		} finally {
			outTr.close();
		}
	}

	/**
	 * 回帰テストの場合、テスト方法を行う。
	 *
	 * @throws Exception
	 *             例外発生時
	 */
	private void doMethodByTr() throws Exception {
		// 処理前、VOのデータ
		Map<String, Object> voMap = new HashMap<String, Object>();
		Map<String, Object> pagingVoMap = new HashMap<String, Object>();
		Map<String, List<Map<String, Object>>> detailVoMap = new HashMap<String, List<Map<String, Object>>>();

		// TRシート「VOデータ」から、VOデータを取得する。
		// String className = readVODataFromTr(voMap, detailVoMap,
		// TR_SHEET_NAME_2);
		String className = readVOData(new XSSFWorkbook(new FileInputStream(
				trFilePath)), voMap, pagingVoMap, detailVoMap,
				new ArrayList<String>(), new HashMap<String, List<String>>(),
				TR_SHEET_NAME_2);

		// VO対象の作成
		Class<?> c = Class.forName(className);
		Object cInstance = c.newInstance();

		// 処理前、VOデータをVO対象にセットする
		getVoData(c, cInstance, voMap, pagingVoMap, detailVoMap);

		// ビジネス処理の行う
		switch (actionType) {
		case DO_EDIT:
			service.doEdit((BaseVO) cInstance);
			break;
		case DO_VALIDATE:
			service.validate((BaseVO) cInstance);
			break;
		case DO_VALIDATE_AND_EDIT:
			service.validateAndEdit((BaseVO) cInstance);
			break;
		default:
			break;
		}

		// VOの期待データ
		Map<String, Object> voMapTemp = new HashMap<String, Object>();
		Map<String, Object> pagingVoMapTemp = new HashMap<String, Object>();
		Map<String, List<Map<String, Object>>> detailVoTempMap = new HashMap<String, List<Map<String, Object>>>();

		// TRシート「期待VO」から、VOデータを取得する。
		// readVODataFromTr(voMapTemp, detailVoTempMap, TR_SHEET_NAME_5);
		readVOData(new XSSFWorkbook(new FileInputStream(trFilePath)),
				voMapTemp, pagingVoMapTemp, detailVoTempMap,
				new ArrayList<String>(), new HashMap<String, List<String>>(),
				TR_SHEET_NAME_5);
		// 期待VO対象の作成
		Object cInstanceTemp = c.newInstance();
		// VOデータをVO期待対象にセットする
		getVoData(c, cInstanceTemp, voMapTemp, pagingVoMapTemp, detailVoTempMap);
		// VO全部のフィールドの取得
		List<Field> fields = getAllFields(c);

		// VOの結果データ
		Map<String, Object> voMapResult = new HashMap<String, Object>();
		Map<String, Object> pagingVoMapResult = new HashMap<String, Object>();
		Map<String, List<Map<String, Object>>> detailMapResult = new HashMap<String, List<Map<String, Object>>>();

		for (Field field : fields) {

			if ("interface java.util.List".equals(field.getType().toString())) {

				String listTotalType = field.getGenericType().toString();
				Class<?> detailClass = Class.forName(listTotalType.substring(
						listTotalType.indexOf("<") + 1,
						listTotalType.indexOf(">")));

				// VOメンバーが明細リストの場合

				// 明細VOのパラメータ名
				String key = field.getName();

				// get方法名の取得
				String methodName = "";

				if (key.substring(1, 2).toUpperCase()
						.equals(key.substring(1, 2))) {
					methodName = "get" + key.substring(0, 1)
							+ key.substring(1, 2).toUpperCase()
							+ key.substring(2);
				} else {
					methodName = "get" + key.substring(0, 1).toUpperCase()
							+ key.substring(1);
				}

				if ("__cobertura_counters".equals(key)) {
					continue;
				}

				// get方法の取得
				Method m = c.getMethod(methodName);
				// get方法の実行
				List<?> detailDataList = (List<?>) m.invoke(cInstance);

				List<Map<String, Object>> value = new ArrayList<Map<String, Object>>();

				for (Object detailData : detailDataList) {

					Map<String, Object> detailDataMap = new HashMap<String, Object>();

					// 明細VOのメンバーの取得
					List<Field> detailfields = getAllFields(detailClass);

					for (Field detailField : detailfields) {

						if ("__cobertura_counters"
								.equals(detailField.getName())) {
							continue;
						}

						// 明細VOメンバー名の取得
						String detailKey = detailField.getName();
						// get方法名の取得
						String detailMethodName = "";

						if (detailKey.substring(1, 2).toUpperCase()
								.equals(detailKey.substring(1, 2))) {
							detailMethodName = "get"
									+ detailKey.substring(0, 1)
									+ detailKey.substring(1, 2).toUpperCase()
									+ detailKey.substring(2);
						} else {
							detailMethodName = "get"
									+ detailKey.substring(0, 1).toUpperCase()
									+ detailKey.substring(1);
						}
						if ("__cobertura_counters".equals(detailKey)) {
							continue;
						}

						// get方法の取得
						Method detailM = detailClass
								.getMethod(detailMethodName);

						if ("class java.util.Date".equals(detailM
								.getReturnType().toString())) {
							// get方法の行う
							Date detailValue = (Date) detailM
									.invoke(detailData);

							detailDataMap.put(
									detailKey,
									(detailValue == null) ? ""
											: (changeDateToString(detailValue,
													"yyyy/MM/dd HH:mm:ss")));

							value.add(detailDataMap);
							continue;
						}

						if ("class java.sql.Timestamp".equals(detailM
								.getReturnType().toString())) {
							// get方法の行う
							Timestamp detailValue = (Timestamp) detailM
									.invoke(detailData);

							detailDataMap.put(
									detailKey,
									(detailValue == null) ? "" : (detailValue
											.toString()));

							value.add(detailDataMap);
							continue;
						}

						if ("String[]".equals(detailM.getReturnType()
								.getSimpleName())) {
							// get方法の行う
							String[] detailValue = (String[]) detailM
									.invoke(detailData);

							detailDataMap
									.put(detailKey,
											(detailValue == null) ? ""
													: (changeStringArrayToString(detailValue)));

							value.add(detailDataMap);
							continue;
						}

						if (("BigDecimal[]").equals(m.getReturnType()
								.getSimpleName())) {
							// get方法の行う
							BigDecimal[] detailValue = (BigDecimal[]) detailM
									.invoke(detailData);

							detailDataMap
									.put(detailKey,
											(detailValue == null) ? ""
													: (changeBigDecimalArrayToString(detailValue)));

							value.add(detailDataMap);
							continue;
						}

						// get方法の行う
						Object detailValue = detailM.invoke(detailData);
						detailDataMap.put(detailKey, (detailValue == null) ? ""
								: (String.valueOf(detailValue)));

						value.add(detailDataMap);
					}

				}

				// 明細VOデータの取得
				detailMapResult.put(key, value);
			} else {
				// VOのメンバー名
				String key = field.getName();

				// get方法名の取得
				String methodName = "get" + key.substring(0, 1).toUpperCase()
						+ key.substring(1);

				if ("__cobertura_counters".equals(key)) {
					continue;
				}

				// get方法の取得
				Method m = c.getMethod(methodName);

				// メンバーが「pagingVO」の場合
				if ("pagingVO".equals(key)) {
					PagingVO pagingVO = (PagingVO) m.invoke(cInstance);

					pagingVoMapResult.put("nowPageCount",
							pagingVO.getNowPageCount());
					pagingVoMapResult.put("pagingLimit",
							pagingVO.getPagingLimit());
					pagingVoMapResult.put("allCount", pagingVO.getAllCount());
					continue;
				}

				if (("class java.util.Date").equals(m.getReturnType()
						.toString())) {
					Date value = (Date) m.invoke(cInstance);
					// VOデータの取得
					voMapResult
							.put(key,
									((value == null) ? "" : changeDateToString(
											value, "yyyy/MM/dd HH:mm:ss")));

					continue;
				}

				if (("class java.sql.Timestamp").equals(m.getReturnType()
						.toString())) {
					Timestamp value = (Timestamp) m.invoke(cInstance);
					// VOデータの取得
					voMapResult.put(key,
							((value == null) ? "" : value.toString()));

					continue;
				}

				if (("String[]").equals(m.getReturnType().getSimpleName())) {
					String[] value = (String[]) m.invoke(cInstance);
					// VOデータの取得
					voMapResult.put(key, ((value == null) ? ""
							: changeStringArrayToString(value)));

					continue;
				}

				if (("BigDecimal[]").equals(m.getReturnType().getSimpleName())) {
					BigDecimal[] value = (BigDecimal[]) m.invoke(cInstance);
					// VOデータの取得
					voMapResult.put(key, ((value == null) ? ""
							: changeBigDecimalArrayToString(value)));

					continue;
				}

				// get方法の実行
				Object value = m.invoke(cInstance);

				// VOデータの取得
				voMapResult.put(key,
						((value == null) ? "" : String.valueOf(value)));
			}
		}

		// 期待VO対象の作成
		Object cInstanceResult = c.newInstance();
		// VOデータをVO期待対象にセットする
		getVoData(c, cInstanceResult, voMapResult, pagingVoMapResult,
				detailMapResult);

		// 処理後VOと期待VOを比較する
		compareVoAllFields(c, fields, cInstanceResult, cInstanceTemp);
	}

	/**
	 * シート「共通情報データ」を作成する。
	 *
	 * @param wb
	 *            　TEまたはTRファイル
	 * @param sheetName
	 *            　シート名前
	 * @param filePath
	 *            ファイルパス
	 * @throws Exception
	 *             例外発生時
	 */
	private void createSheetCommonInfoData(XSSFWorkbook wb, String sheetName,
			String filePath) throws Exception {
		// シート「共通情報データ」の取得
		XSSFSheet sheet = wb.getSheet(sheetName);

		// LogonInfoのキー
		String[] logonInfoKeys = new String[] { "userId", "userName",
				"lv10SosikiCd", "shozokuKojoCode", "shozokuSaibanId",
				"shozokuKojoName", "bujoName", "logonTime", "seizoshiyoSansho",
				"seizoshiyoToroku", "reienKotei", "mekiKotei", "colorKotei",
				"shutantoKotei", "kakuninKengen", "shinsaKengen",
				"shinsaDaikoKengen", "shoninKengen", "shoninDaikoKengen",
				"kakuninUserid", "shinsaUserid", "shinsaDaikosha",
				"shoninUserid", "shoninDaikosha", "juryoGroup", "tantoKotei",
				"kouteiGyomuAuth", "seizoGyomuAuth", "hinsituHosyoGyomuAuth",
				"buturyuGyomuAuth", "zissekiKanriGyomuAuth",
				"jisekiKanriTokusyuAuthKakouDateKirikaeRun",
				"jisekiKanriTokusyuAuthJisekiSyukei",
				"jisekiKanriTokusyuAuthMonthlyKakutei",
				"jisekiKanriTokusyuAuthDataOutput", "honsyaSeisanKanriAuth",
				"eigyoAuth", "kouteiSyomuTanto", "kouteiGyomuSyutanto",
				"kouteiTokusyuAuthKyuPositionLocationHenko",
				"kouteiTokusyuAuthCoilInfoHenko", "seizoGyomuTanto",
				"seizoGyomuSyutanto", "sinsaAuthKoutei",
				"sinsaDaikoAuthKoutei", "syoninAuthKoutei",
				"syoninDaikoAuthKoutei", "sinsaUserKoutei",
				"sinsaDaikoUserKoutei", "syoninUserKoutei",
				"syoninDaikoUserKoutei", "kouteiKanriTotalGyomuSyutanto" };
		// LogonInfoキーのコメント
		String[] logonInfoComments = new String[] { "ユーザID", "ユーザ名", "事業所コード",
				"所属工場コード", "所属工場の採番ID", "所属工場名", "所属部署名", "ログイン日時",
				"製造仕様書参照権限", "製造仕様書登録権限", "冷延工程", "めっき工程", "カラー工程", "主担当工程",
				"作成確認権限", "審査権限", "審査代行権限", "承認権限", "承認代行権限", "作成確認者", "審査者",
				"審査代行者", "承認者", "承認代行者", "受領グループ", "担当工程", "工程業務権限", "製造業務権限",
				"品質保証業務権限", "物流業務権限", "実績管理業務権限", "実績管理業務特殊権限(加工日切替実行)",
				"実績管理業務特殊権限(実績集計)", "実績管理業務特殊権限(月次確定)", "実績管理業務特殊権限(データ出力)",
				"本社生産管理権限", "営業権限", "工程業務担当", "主担当(工程業務)",
				"工程業務特殊権限(級・ポジション・置場変更)", "工程業務特殊権限(コイル情報変更)", "製造業務担当",
				"主担当(製造業務)", "審査権限(工程業務)", "審査代行権限(工程業務)", "承認権限(工程業務)",
				"承認代行権限(工程業務)", "審査者(工程業務)", "審査代行者(工程業務)", "承認者(工程業務)",
				"承認代行者(工程業務)", "主担当(工程管理全業務)" };
		// CommonInfoのキー
		String[] commonInfoKeys = new String[] { "serverAddress", "serverHost" };
		// CommonInfoキーのコメント
		String[] commonInfoComments = new String[] { "サーバIPアドレス", "サーバホスト名" };

		// 共通情報「LogonInfo」の作成
		createCommonInfoData(filePath, sheet, 7, logonInfoPath, logonInfoKeys,
				logonInfoComments);
		// 共通情報「CommonInfo」の作成
		createCommonInfoData(filePath, sheet, 12, commonInfoPath,
				commonInfoKeys, commonInfoComments);

	}

	/**
	 * 共通情報を作成する。
	 *
	 * @param filePath
	 *            　TEまたはTRファイルパス
	 * @param sheet
	 *            　シート「共通情報データ」
	 * @param rowNum
	 *            　行数
	 * @param voPath
	 *            　VOのパッケージパス
	 * @param keys
	 *            　共通情報のキー
	 * @param comments
	 *            　共通情報のコメント
	 * @throws Exception
	 *             例外発生時
	 */
	private void createCommonInfoData(String filePath, XSSFSheet sheet,
			int rowNum, String voPath, String[] keys, String[] comments)
			throws Exception {
		XSSFRow titleRow = sheet.createRow(rowNum);

		XSSFCell cell;
		XSSFCell keyCell;

		// タイトル行
		keyCell = titleRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("VO");

		cell = titleRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(filePath).get(1));
		cell.setCellValue(voPath);

		// キー行
		rowNum++;
		XSSFRow keyRow = sheet.createRow(rowNum);

		keyCell = keyRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("フィールド");

		// キー行の作成
		for (int i = 0; i < keys.length; i++) {
			keyCell = keyRow.createCell(i + 1);
			keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

			keyCell.setCellStyle(cellStyleMap.get(filePath).get(2));
			keyCell.setCellValue(keys[i]);
		}

		// コメント行
		rowNum++;
		XSSFRow commentRow = sheet.createRow(rowNum);

		keyCell = commentRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("項目名");

		// コメント行の作成
		for (int i = 0; i < comments.length; i++) {
			keyCell = commentRow.createCell(i + 1);
			keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

			keyCell.setCellStyle(cellStyleMap.get(filePath).get(3));
			keyCell.setCellValue(comments[i]);
		}

		rowNum++;
		XSSFRow valueRow = sheet.createRow(rowNum);

		keyCell = valueRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("-");

		// 共通情報「LogonInfo」の場合
		if (voPath.contains("LogonInfo")) {
			for (int i = 0; i < keys.length; i++) {
				String methodName = "get"
						+ keys[i].substring(0, 1).toUpperCase()
						+ keys[i].substring(1);
				Method m = LogonInfo.class.getMethod(methodName);
				Object value = m.invoke(InfoUtil.getLogonInfo());

				keyCell = valueRow.createCell(i + 1);
				keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

				keyCell.setCellStyle(cellStyleMap.get(filePath).get(4));
				keyCell.setCellValue(((value == null) ? "" : (String
						.valueOf(value))));
			}
		} else {
			// 共通情報「CommonInfo」の場合

			// サーバIPアドレス
			String serverAddress = InfoUtil.getCommonInfo().getServerAddress();
			// サーバホスト名
			String serverHost = InfoUtil.getCommonInfo().getServerHost();

			keyCell = valueRow.createCell(1);
			keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			keyCell.setCellStyle(cellStyleMap.get(filePath).get(4));
			keyCell.setCellValue(serverAddress == null ? "" : serverAddress);

			keyCell = valueRow.createCell(2);
			keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
			keyCell.setCellStyle(cellStyleMap.get(filePath).get(4));
			keyCell.setCellValue(serverHost == null ? "" : serverHost);
		}

		rowNum++;
		rowNum++;

		// END行
		XSSFRow endRow = sheet.createRow(rowNum);
		keyCell = endRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellValue("END");
	}

	/**
	 * 処理前と処理後のDBデータシートを作成する。
	 *
	 * @param wbTe
	 *            TEファイル
	 * @param wbTr
	 *            TRファイル
	 * @param processType
	 *            true:処理前の場合 false:処理後の場合
	 * @throws Exception
	 *             例外発生時
	 */
	private void createSheetForInAndOutDB(XSSFWorkbook wbTe, XSSFWorkbook wbTr,
			boolean processType) throws Exception {

		XSSFSheet sheetTe;
		XSSFSheet sheetTr;

		// 処理前の場合
		if (processType) {
			sheetTe = wbTe.getSheet(TE_SHEET_NAME_2);
			sheetTr = wbTr.getSheet(TR_SHEET_NAME_1);
		} else {
			sheetTe = wbTe.getSheet(TE_SHEET_NAME_3);
			sheetTr = wbTr.getSheet(TR_SHEET_NAME_4);
		}

		int i = 6;

		for (String tableName : tableNames) {

			// 行の追加
			XSSFRow titleRowTe = sheetTe.createRow(i);
			XSSFRow titleRowTr = sheetTr.createRow(i);
			i++;

			// テーブルタイトル作成
			writeTableTitle(teFilePath, sheetTe, titleRowTe, tableName);
			writeTableTitle(trFilePath, sheetTr, titleRowTr, tableName);

			// テーブルヘッダ作成
			i = writeTableHead(sheetTe, sheetTr, tableName, i);

			// テーブル明細データ作成
			i = writeTableDetailData(sheetTe, sheetTr, tableName, i);

			i++;
		}

		// END行の作成
		XSSFRow endRowTe = sheetTe.createRow(i);
		XSSFCell endCellTe = endRowTe.createCell(0);
		endCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		endCellTe.setCellValue("END");

		XSSFRow endRowTr = sheetTr.createRow(i);
		XSSFCell endCellTr = endRowTr.createCell(0);
		endCellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
		endCellTr.setCellValue("END");
	}

	/**
	 * テスト方法を行う。
	 *
	 * @param wbTe
	 *            TEファイル
	 * @param wbTr
	 *            TRファイル
	 * @throws Exception
	 *             例外発生時
	 */
	private void doMethodForCreateTrAndTe(XSSFWorkbook wbTe, XSSFWorkbook wbTr)
			throws Exception {
		InputStream io = new FileInputStream(tpFilePath);

		// TPファイルの取得
		XSSFWorkbook wb = new XSSFWorkbook(io);

		// VOデータ
		Map<String, Object> voMap = new HashMap<String, Object>();
		// PaingVOデータ
		Map<String, Object> pagingVoMap = new HashMap<String, Object>();
		// 明細VOデータ
		Map<String, List<Map<String, Object>>> detailVoMap = new HashMap<String, List<Map<String, Object>>>();
		// 明細VO
		List<Map<String, Object>> detailVoList = new ArrayList<Map<String, Object>>();

		// EXCELファイルから、VOデータの取得を行う。
		String className = readVOData(wb, voMap, pagingVoMap, detailVoMap,
				new ArrayList<String>(), new HashMap<String, List<String>>(),
				TP_SHEET_NAME_3);

		// VO対象の作成
		Class<?> c = Class.forName(className);
		Object cInstance = c.newInstance();

		for (String key : voMap.keySet()) {

			String setMethod = "";

			if (key.length() > 1) {

				String secondLetter = key.substring(1, 2);

				// set方法の取得
				if (!StringUtils.isNumeric(secondLetter)
						&& (!secondLetter.toUpperCase().equals(secondLetter))) {
					setMethod = "set" + key.substring(0, 1).toUpperCase()
							+ key.substring(1);
				} else {
					setMethod = "set" + key;
				}
			} else {
				setMethod = "set" + key.substring(0, 1).toUpperCase()
						+ key.substring(1);
			}

			// パラメータタイプ
			Class<?> paramType = getParamType(c, key);
			// set方法の取得
			Method m = c.getMethod(setMethod, paramType);
			// VOデータをVOにセットする
			if ("int".equals(paramType.toString())) {
				m.invoke(cInstance,
						Integer.valueOf(String.valueOf(voMap.get(key))));
			} else if ("class java.math.BigDecimal"
					.equals(paramType.toString())) {

				if (!StringUtils.isBlank(String.valueOf(voMap.get(key)))) {
					m.invoke(cInstance,
							new BigDecimal(String.valueOf(voMap.get(key))));
				}
			} else if ("class java.util.Date".equals(paramType.toString())) {
				m.invoke(
						cInstance,
						((voMap.get(key) == null) || ("".equals(String
								.valueOf(voMap.get(key))))) ? null
								: changeStringToDate(
										String.valueOf(voMap.get(key)),
										"yyyy/MM/dd HH:mm:ss"));
			} else if ("class java.sql.Timestamp".equals(paramType.toString())) {
				m.invoke(cInstance, ((voMap.get(key) == null) || (""
						.equals(String.valueOf(voMap.get(key))))) ? null
						: Timestamp.valueOf(String.valueOf(voMap.get(key))));
			} else if ("String[]".equals(paramType.getSimpleName())) {
				Object value = new Object();

				if (StringUtils.isBlank(String.valueOf(voMap.get(key)))) {
					value = null;
				} else {
					value = String.valueOf(voMap.get(key)).split(",");
				}

				m.invoke(cInstance, value);
			} else if ("Integer".equals(paramType.getSimpleName())) {

				if (StringUtils.isNotBlank(String.valueOf(voMap.get(key)))) {
					m.invoke(cInstance,
							Integer.valueOf(String.valueOf(voMap.get(key))));
				}
			} else if ("byte[]".equals(paramType.getSimpleName())) {
				Object value = new Object();
				if (StringUtils.isBlank(String.valueOf(voMap.get(key)))) {
					value = null;
				} else {
					value = String.valueOf(voMap.get(key)).split(",");
				}

				m.invoke(cInstance, value);
			} else if ("BigDecimal[]".equals(paramType.getSimpleName())) {
				Object value = new Object();

				if (StringUtils.isBlank(String.valueOf(voMap.get(key)))) {
					value = null;
				} else {

					String[] strArr = String.valueOf(voMap.get(key)).split(",");
					BigDecimal[] bigArr = new BigDecimal[strArr.length];
					for (int i = 0; i < strArr.length; i++) {
						bigArr[i] = new BigDecimal(strArr[i]);
					}
					value = bigArr;
				}

				m.invoke(cInstance, value);
			} else {
				m.invoke(cInstance, paramType.cast(voMap.get(key)));
			}
		}

		if (!pagingVoMap.keySet().isEmpty()) {
			PagingVO pagingVO = new PagingVO();

			pagingVO.setNowPageCount((pagingVoMap.get("nowPageCount") == null) ? ""
					: String.valueOf(pagingVoMap.get("nowPageCount")));
			pagingVO.setAllCount((pagingVoMap.get("allCount") == null) ? ""
					: String.valueOf(pagingVoMap.get("allCount")));
			pagingVO.setPagingLimit((pagingVoMap.get("pagingLimit") == null) ? ""
					: String.valueOf(pagingVoMap.get("pagingLimit")));

			Method m = c.getMethod("setPagingVO", PagingVO.class);

			m.invoke(cInstance, pagingVO);
		}

		// // 明細データある場合
		// if (hasDetailFlag) {

		for (String detailName : detailVoMap.keySet()) {
			List<Object> detail = new ArrayList<Object>();

			// 明細のタイプの取得
			Field field = getCurField(c, detailName);
			String listTotalType = field.getGenericType().toString();
			Class<?> detailClass = Class
					.forName(listTotalType.substring(
							listTotalType.indexOf("<") + 1,
							listTotalType.indexOf(">")));

			// VO明細データの取得
			detailVoList = detailVoMap.get(detailName);

			for (Map<String, Object> detailData : detailVoList) {
				// 明細VO対象の作成
				Object detailInstance = detailClass.newInstance();

				for (String key : detailData.keySet()) {

					// set方法名の取得
					String setMethod = "";

					if (key.substring(1, 2).toUpperCase()
							.equals(key.substring(1, 2))) {
						setMethod = "set" + key.substring(0, 1)
								+ key.substring(1, 2).toUpperCase()
								+ key.substring(2);
					} else {
						setMethod = "set" + key.substring(0, 1).toUpperCase()
								+ key.substring(1);
					}

					Method[] methods = detailClass.getMethods();
					for (Method m : methods) {
						// set方法の取得
						if (setMethod.equals(m.getName())) {
							Class<?>[] paraType = m.getParameterTypes();

							// メンバータイプがintの場合
							if ("int".equals(paraType[0].toString())) {
								// set方法の実行
								m.invoke(detailInstance, Integer.valueOf(String
										.valueOf(detailData.get(key))));
							} else if ("class java.math.BigDecimal"
									.equals(paraType[0].toString())) {

								if (!StringUtils.isBlank(String
										.valueOf(detailData.get(key)))) {
									m.invoke(
											detailInstance,
											new BigDecimal(String
													.valueOf(detailData
															.get(key))));
								}
							} else if ("class java.util.Date"
									.equals(paraType[0].toString())) {
								m.invoke(
										detailInstance,
										((detailData.get(key) == null) || (""
												.equals(String
														.valueOf(detailData
																.get(key))))) ? null
												: changeStringToDate(String
														.valueOf(detailData
																.get(key)),
														"yyyy/MM/dd HH:mm:ss"));
							} else if ("class java.sql.Timestamp"
									.equals(paraType[0].toString())) {
								m.invoke(
										detailInstance,
										((detailData.get(key) == null) || (""
												.equals(String
														.valueOf(detailData
																.get(key))))) ? null
												: Timestamp.valueOf(String
														.valueOf(detailData
																.get(key))));
							} else if ("String[]".equals(paraType[0]
									.getSimpleName())) {
								Object value = new Object();
								if (StringUtils.isBlank(String
										.valueOf(detailData.get(key)))) {
									value = null;
								} else {
									value = String.valueOf(detailData.get(key))
											.split(",");
								}

								m.invoke(detailInstance, value);
							} else {
								// set方法の実行
								m.invoke(detailInstance,
										paraType[0].cast(detailData.get(key)));
							}
							break;
						}
					}
				}

				detail.add(detailInstance);
			}

			// 明細のset方法名の取得
			String setMethod = "set" + detailName.substring(0, 1).toUpperCase()
					+ detailName.substring(1);

			Method[] methods = c.getMethods();
			for (Method m : methods) {
				// set方法の取得
				if (setMethod.equals(m.getName())) {
					// set方法の実行
					m.invoke(cInstance, detail);
					break;
				}
			}
		}
		// }

		// 処理前VOデータシートの作成
		createInputAndOutputVoSheet(wbTe, wbTr, c, cInstance, true);

		// ビジネス処理の行う
		switch (actionType) {
		case DO_EDIT:
			service.doEdit((BaseVO) cInstance);
			break;
		case DO_VALIDATE:
			service.validate((BaseVO) cInstance);
			break;
		case DO_VALIDATE_AND_EDIT:
			service.validateAndEdit((BaseVO) cInstance);
			break;
		default:
			break;
		}

		// 処理後VOデータシートの作成
		createInputAndOutputVoSheet(wbTe, wbTr, c, cInstance, false);
	}

	/**
	 * 処理前と処理後VOデータシートを作成する。
	 *
	 * @param wbTe
	 *            TEファイル
	 * @param wbTr
	 *            TRファイル
	 * @param c
	 *            VOのクラス
	 * @param cInstance
	 *            VO対象
	 * @param inputOrOutputFlag
	 *            true:処理前の場合 false:処理後の場合
	 * @throws Exception
	 *             例外発生時
	 */
	private void createInputAndOutputVoSheet(XSSFWorkbook wbTe,
			XSSFWorkbook wbTr, Class<?> c, Object cInstance,
			boolean inputOrOutputFlag) throws Exception {

		XSSFSheet sheetTe;
		XSSFSheet sheetTr;

		// 処理前の場合
		if (inputOrOutputFlag) {
			sheetTe = wbTe.getSheet(TE_SHEET_NAME_4);
			sheetTr = wbTr.getSheet(TR_SHEET_NAME_2);
		} else {
			sheetTe = wbTe.getSheet(TE_SHEET_NAME_6);
			sheetTr = wbTr.getSheet(TR_SHEET_NAME_5);
		}

		// ページングVO
		PagingVO pagingVO = null;
		// VOデータ
		Map<String, Object> voMap = new HashMap<String, Object>();
		// 明細VOデータ
		Map<String, List<?>> detailMap = new HashMap<String, List<?>>();
		// VOデータのキーリスト
		List<String> keyList = new ArrayList<String>();
		int i = 0;

		// VO全部のメンバーの取得
		List<Field> fields = getAllFields(c);

		for (Field field : fields) {

			if ("interface java.util.List".equals(field.getType().toString())) {
				// VOメンバーが明細リストの場合

				// 明細VOのパラメータ名
				String key = field.getName();

				// get方法名の取得
				String methodName = "";

				if (key.substring(1, 2).toUpperCase()
						.equals(key.substring(1, 2))) {
					methodName = "get" + key.substring(0, 1)
							+ key.substring(1, 2).toUpperCase()
							+ key.substring(2);
				} else {
					methodName = "get" + key.substring(0, 1).toUpperCase()
							+ key.substring(1);
				}

				if ("__cobertura_counters".equals(key)) {
					continue;
				}

				// get方法の取得
				Method m = c.getMethod(methodName);
				// get方法の実行
				List<?> value = (List<?>) m.invoke(cInstance);

				// 明細VOデータの取得
				detailMap.put(key, value);
			} else {
				// VOのメンバー名
				String key = field.getName();

				// get方法名の取得
				String methodName = "";

				if (key.length() > 1) {

					String secondLetter = key.substring(1, 2);

					// set方法の取得
					if (!StringUtils.isNumeric(secondLetter)
							&& (secondLetter.toUpperCase().equals(secondLetter))) {
						methodName = "get" + key;
					} else {
						methodName = "get" + key.substring(0, 1).toUpperCase()
								+ key.substring(1);
					}
				} else {
					methodName = "get" + key.substring(0, 1).toUpperCase()
							+ key.substring(1);
				}

				if ("__cobertura_counters".equals(key)) {
					continue;
				}

				// get方法の取得
				Method m = c.getMethod(methodName);

				// メンバーが「pagingVO」の場合
				if ("pagingVO".equals(key)) {
					pagingVO = (PagingVO) m.invoke(cInstance);

					continue;
				}

				if (("class java.util.Date").equals(m.getReturnType()
						.toString())) {
					// VOキーの取得
					keyList.add(i, key);
					i++;

					Date value = (Date) m.invoke(cInstance);

					// VOデータの取得
					voMap.put(
							key,
							((value == null) ? "" : changeDateToString(value,
									"yyyy/MM/dd HH:mm:ss")));

					continue;
				}

				if (("class java.sql.Timestamp").equals(m.getReturnType()
						.toString())) {
					// VOキーの取得
					keyList.add(i, key);
					i++;

					Timestamp value = (Timestamp) m.invoke(cInstance);

					// VOデータの取得
					voMap.put(key, ((value == null) ? "" : value.toString()));

					continue;
				}

				if (("String[]").equals(m.getReturnType().getSimpleName())) {
					// VOキーの取得
					keyList.add(i, key);
					i++;

					String[] value = (String[]) m.invoke(cInstance);

					// VOデータの取得
					voMap.put(key, ((value == null) ? ""
							: changeStringArrayToString(value)));

					continue;
				}

				if (("BigDecimal[]").equals(m.getReturnType().getSimpleName())) {
					// VOキーの取得
					keyList.add(i, key);
					i++;

					BigDecimal[] value = (BigDecimal[]) m.invoke(cInstance);

					// VOデータの取得
					voMap.put(key, ((value == null) ? ""
							: changeBigDecimalArrayToString(value)));

					continue;
				}

				// get方法の実行
				Object value = m.invoke(cInstance);

				// VOキーの取得
				keyList.add(i, key);
				i++;

				// VOデータの取得
				voMap.put(key, ((value == null) ? "" : String.valueOf(value)));
			}
		}

		int rowNo = 6;

		// 行の作成
		XSSFRow rowTe = sheetTe.createRow(rowNo);
		XSSFRow rowTr = sheetTr.createRow(rowNo);
		rowNo++;

		// VOタイトルの作成
		writeVOTitle(teFilePath, rowTe, c.getName());
		writeVOTitle(trFilePath, rowTr, c.getName());

		// 行の作成
		rowTe = sheetTe.createRow(rowNo);
		rowTr = sheetTr.createRow(rowNo);
		rowNo++;

		// VOヘッダの作成
		writeVOHead(teFilePath, rowTe, keyList);
		writeVOHead(trFilePath, rowTr, keyList);

		// 行の作成
		rowTe = sheetTe.createRow(rowNo);
		rowTr = sheetTr.createRow(rowNo);
		rowNo++;

		// VOコメントの作成
		writeVOComment(teFilePath, rowTe, keyList);
		writeVOComment(trFilePath, rowTr, keyList);

		// VOデータの作成
		rowNo = writeVOData(sheetTe, sheetTr, keyList, voMap, rowNo);

		// ブランク行の作成
		sheetTe.createRow(rowNo);
		sheetTr.createRow(rowNo);
		rowNo++;

		// ページングVOがNULL場合
		if (pagingVO != null) {
			// ページングVOの作成
			rowNo = writePagingVOData(sheetTe, sheetTr, pagingVO, rowNo);

			// ブランク行の作成
			sheetTe.createRow(rowNo);
			sheetTr.createRow(rowNo);
			rowNo++;
		}

		for (String paraName : detailMap.keySet()) {
			// 明細VOタイプの取得
			Field field = getCurField(c, paraName);
			String totalName = field.getGenericType().toString();
			String detailTypeName = totalName.substring(
					totalName.indexOf("<") + 1, totalName.indexOf(">"));

			// 明細のクラス
			Class<?> detailClass = Class.forName(detailTypeName);

			// 行の作成
			rowTe = sheetTe.createRow(rowNo);
			rowTr = sheetTr.createRow(rowNo);
			rowNo++;

			// 明細VOのタイトルの作成
			writeDetailVOTitle(teFilePath, rowTe, detailClass, paraName);
			writeDetailVOTitle(trFilePath, rowTr, detailClass, paraName);

			// 行の作成
			rowTe = sheetTe.createRow(rowNo);
			rowTr = sheetTr.createRow(rowNo);
			rowNo++;

			// 明細VOのヘッダの作成
			writeDetailVOHead(teFilePath, rowTe, detailClass);
			writeDetailVOHead(trFilePath, rowTr, detailClass);

			// 行の作成
			rowTe = sheetTe.createRow(rowNo);
			rowTr = sheetTr.createRow(rowNo);
			rowNo++;

			// 明細VOのコメントの作成
			writeDetailVOComment(teFilePath, rowTe, detailClass);
			writeDetailVOComment(trFilePath, rowTr, detailClass);

			// 明細VOのデータの作成
			rowNo = writeDetailVOListData(sheetTe, sheetTr,
					detailMap.get(paraName), rowNo, detailClass);

			// ブランク行の作成
			sheetTe.createRow(rowNo);
			sheetTr.createRow(rowNo);
			rowNo++;
		}

		// END行の作成
		XSSFRow endRowTe = sheetTe.createRow(rowNo);
		XSSFCell endCellTe = endRowTe.createCell(0);
		endCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		endCellTe.setCellValue("END");

		XSSFRow endRowTr = sheetTr.createRow(rowNo);
		XSSFCell endCellTr = endRowTr.createCell(0);
		endCellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
		endCellTr.setCellValue("END");
	}

	/**
	 * テーブルタイトルを作成する。
	 *
	 * @param filePath
	 *            EXCELファイルパス
	 * @param sheet
	 *            EXCELシート
	 * @param titleRow
	 *            タイトル行
	 * @param tableName
	 *            テーブル名
	 * @throws Exception
	 *             例外発生時
	 */
	private void writeTableTitle(String filePath, XSSFSheet sheet,
			XSSFRow titleRow, String tableName) throws Exception {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = titleRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("TABLE");

		cell = titleRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(filePath).get(1));
		cell.setCellValue(tableName.toUpperCase());
	}

	/**
	 * VOタイトルを作成する。
	 *
	 * @param filePath
	 *            EXCELファイルパス
	 * @param titleRow
	 *            EXCELタイトル行
	 * @param className
	 *            VOクラス名
	 */
	private void writeVOTitle(String filePath, XSSFRow titleRow,
			String className) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = titleRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("VO");

		cell = titleRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(filePath).get(1));
		cell.setCellValue(className);
	}

	/**
	 * 　明細VOのタイトルを作成する。
	 *
	 * @param wb
	 *            EXCELファイル
	 * @param titleRow
	 *            タイトル行
	 * @param detailClass
	 *            明細VOデータクラス
	 * @param paraName
	 *            明細VOデータメンバー名
	 */
	private void writeDetailVOTitle(String filePath, XSSFRow titleRow,
			Class<?> detailClass, String paraName) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = titleRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("DETAIL");

		cell = titleRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(filePath).get(1));
		cell.setCellValue(paraName);

		cell = titleRow.createCell(2);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(filePath).get(1));
		cell.setCellValue(detailClass.getName());
	}

	/**
	 * テーブルヘッダを作成する。
	 *
	 * @param sheetTe
	 *            TEのシート
	 * @param sheetTr
	 *            TRのシート
	 * @param tableName
	 *            テーブル名
	 * @param lineNo
	 *            行No
	 * @return 行No
	 * @throws Exception
	 *             例外発生時
	 */
	private int writeTableHead(XSSFSheet sheetTe, XSSFSheet sheetTr,
			String tableName, int lineNo) throws Exception {
		List<String> pKeyList = new ArrayList<String>();

		XSSFRow headRowTe = sheetTe.createRow(lineNo);
		XSSFRow headRowTr = sheetTr.createRow(lineNo);
		lineNo++;

		XSSFRow headRowCommentTe = sheetTe.createRow(lineNo);
		XSSFRow headRowCommentTr = sheetTr.createRow(lineNo);
		lineNo++;

		XSSFCell cellTe;
		XSSFCell keyCellTe;
		XSSFCell cellTr;
		XSSFCell keyCellTr;

		keyCellTe = headRowCommentTe.createCell(0);
		keyCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCellTe.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCellTe.setCellValue("項目名");

		keyCellTe = headRowTe.createCell(0);
		keyCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCellTe.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCellTe.setCellValue("COLUMN");

		keyCellTr = headRowCommentTr.createCell(0);
		keyCellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCellTr.setCellStyle(cellStyleMap.get(trFilePath).get(0));
		keyCellTr.setCellValue("項目名");

		keyCellTr = headRowTr.createCell(0);
		keyCellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCellTr.setCellStyle(cellStyleMap.get(trFilePath).get(0));
		keyCellTr.setCellValue("COLUMN");

		// テーブル主キーの取得
		readPrimaryKey(pKeyList, tableName);

		// テーブルキーID、コメントとキー名の取得
		String sql = "";
		sql += "SELECT ";
		sql += "    COL.COLUMN_NAME AS COLUMN_NAME , ";
		sql += "    DES.DESCRIPTION AS COMMENTS ";
		sql += "FROM ";
		sql += "    INFORMATION_SCHEMA.COLUMNS COL LEFT JOIN PG_DESCRIPTION DES ";
		sql += "        ON COL.TABLE_NAME::REGCLASS = DES.OBJOID ";
		sql += "    AND COL.ORDINAL_POSITION = DES.OBJSUBID ";
		sql += "WHERE ";
		sql += "    TABLE_SCHEMA = 'yodo_tran' ";
		sql += "    AND TABLE_NAME = '" + tableName.toLowerCase() + "' ";
		sql += "ORDER BY ";
		sql += "    ORDINAL_POSITION; ";

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		int j = 1;

		while (rs.next()) {

			// TEテーブルキー名の書く
			cellTe = headRowTe.createCell(j);
			cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
			// 主キーの場合
			if (pKeyList.contains(rs.getString("COLUMN_NAME"))) {
				cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(5));
			} else {
				cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(2));
			}
			cellTe.setCellValue(rs.getString("COLUMN_NAME"));

			// TRテーブルキー名の書く
			cellTr = headRowTr.createCell(j);
			cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
			// 主キーの場合
			if (pKeyList.contains(rs.getString("COLUMN_NAME"))) {
				cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(5));
			} else {
				cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(2));
			}
			cellTr.setCellValue(rs.getString("COLUMN_NAME"));

			// TEテーブルコメントの書く
			cellTe = headRowCommentTe.createCell(j);
			cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
			// 主キーの場合
			if (pKeyList.contains(rs.getString("COLUMN_NAME"))) {
				cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(6));
			} else {
				cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(3));
			}
			cellTe.setCellValue(rs.getString("COMMENTS"));

			// TRテーブルコメントの書く
			cellTr = headRowCommentTr.createCell(j);
			cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
			// 主キーの場合
			if (pKeyList.contains(rs.getString("COLUMN_NAME"))) {
				cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(6));
			} else {
				cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(3));
			}
			cellTr.setCellValue(rs.getString("COMMENTS"));
			j++;
		}

		return lineNo;
	}

	/**
	 * VOヘッダを作成する。
	 *
	 * @param filePath
	 *            EXCELファイルパス
	 * @param row
	 *            EXCEL行
	 * @param keyList
	 *            VOキーリスト
	 */
	private void writeVOHead(String filePath, XSSFRow row, List<String> keyList) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = row.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("COLUMN");

		for (int i = 0; i < keyList.size(); i++) {

			cell = row.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(filePath).get(2));
			cell.setCellValue(keyList.get(i));
		}

	}

	/**
	 * 明細VOのヘッダを作成する。
	 *
	 * @param filePath
	 *            EXCELファイルパス
	 * @param row
	 *            明細VOのヘッダ行
	 * @param detailClass
	 *            明細VOのクラス
	 */
	private void writeDetailVOHead(String filePath, XSSFRow row,
			Class<?> detailClass) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = row.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("COLUMN");

		// 明細VOのメンバーの取得
		List<Field> fields = getAllFields(detailClass);

		int i = 0;

		for (Field field : fields) {

			if ("__cobertura_counters".equals(field.getName())) {
				continue;
			}

			cell = row.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(filePath).get(2));
			cell.setCellValue(field.getName());

			i++;
		}
	}

	/**
	 * VOコメントを作成する。
	 *
	 * @param filePath
	 *            EXCELファイルパス
	 * @param row
	 *            EXCEL行
	 * @param keyList
	 *            VOキーリスト
	 */
	private void writeVOComment(String filePath, XSSFRow row,
			List<String> keyList) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = row.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("項目名");

		for (int i = 0; i < keyList.size(); i++) {
			cell = row.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(filePath).get(3));
			cell.setCellValue("");
		}
	}

	/**
	 * 明細VOのコメントを作成する。
	 *
	 * @param filePath
	 *            EXCELファイルパス
	 * @param row
	 *            明細VOのヘッダ行
	 * @param detailClass
	 *            明細VOのクラス
	 */
	private void writeDetailVOComment(String filePath, XSSFRow row,
			Class<?> detailClass) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = row.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("項目名");

		// 明細VOのメンバーの取得
		List<Field> fields = getAllFields(detailClass);

		int i = 0;

		for (Field field : fields) {

			if ("__cobertura_counters".equals(field.getName())) {
				continue;
			}

			cell = row.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(filePath).get(3));
			cell.setCellValue("");

			i++;
		}
	}

	/**
	 * テーブル明細データを作成する。
	 *
	 * @param sheetTe
	 *            TEのシート
	 * @param sheetTr
	 *            TRのシート
	 * @param tableName
	 *            テーブル名
	 * @param lineNo
	 *            行No
	 * @return 行No
	 * @throws Exception
	 *             例外発生時
	 */
	private int writeTableDetailData(XSSFSheet sheetTe, XSSFSheet sheetTr,
			String tableName, int lineNo) throws Exception {
		XSSFCell cellTe;
		XSSFCell keyCellTe;
		XSSFCell cellTr;
		XSSFCell keyCellTr;

		// テーブルのデータを取得する。
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM "
				+ tableName.toUpperCase());
		ResultSet rs = pstmt.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();

		int count = 0;
		while (rs.next()) {
			count++;

			XSSFRow rowListTe = sheetTe.createRow(lineNo);
			XSSFRow rowListTr = sheetTr.createRow(lineNo);
			lineNo++;

			keyCellTe = rowListTe.createCell(0);
			keyCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
			keyCellTe.setCellStyle(cellStyleMap.get(teFilePath).get(0));
			keyCellTe.setCellValue(count);

			keyCellTr = rowListTr.createCell(0);
			keyCellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
			keyCellTr.setCellStyle(cellStyleMap.get(trFilePath).get(0));
			keyCellTr.setCellValue(count);

			int columnCount = rsmd.getColumnCount();

			for (int k = 1; k <= columnCount; k++) {

				cellTe = rowListTe.createCell(k);
				cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
				cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(4));

				cellTr = rowListTr.createCell(k);
				cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
				cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(4));

				// テーブルキーがDATEの場合
				if ("date".equals(rsmd.getColumnTypeName(k).toLowerCase())) {
					Date value = rs.getDate(rsmd.getColumnName(k));

					if (value != null) {
						cellTe.setCellValue(changeDateToString(value,
								"yyyy/MM/dd HH:mm:ss"));
						cellTr.setCellValue(changeDateToString(value,
								"yyyy/MM/dd HH:mm:ss"));
					} else {
						cellTe.setCellValue("");
						cellTr.setCellValue("");
					}
				} else if ("timestamp".equals(rsmd.getColumnTypeName(k)
						.toLowerCase())) {
					Timestamp value = rs.getTimestamp(rsmd.getColumnName(k));

					if (value != null) {
						cellTe.setCellValue(changeDateToString(value,
								"yyyy/MM/dd HH:mm:ss.SSS"));
						cellTr.setCellValue(changeDateToString(value,
								"yyyy/MM/dd HH:mm:ss.SSS"));
					} else {
						cellTe.setCellValue("");
						cellTr.setCellValue("");
					}
				} else {
					String value = rs.getString(rsmd.getColumnName(k));

					if ("".equals(value)) {
						cellTe.setCellValue("''");
						cellTr.setCellValue("''");
					} else {
						cellTe.setCellValue((value == null) ? "" : value);
						cellTr.setCellValue((value == null) ? "" : value);
					}
				}
			}
		}

		return lineNo;
	}

	/**
	 * VOデータを作成する。
	 *
	 * @param wbTe
	 *            TEのEXCELファイル
	 * @param sheetTe
	 *            TEファイルのシート
	 * @param wbTr
	 *            TRのEXCELファイル
	 * @param sheetTr
	 *            TRファイルのシート
	 * @param keyList
	 *            VOのキーリスト
	 * @param voMap
	 *            VOデータ
	 * @param rowNo
	 *            行No
	 * @return 行No
	 * @throws Exception
	 *             例外発生時
	 */
	private int writeVOData(XSSFSheet sheetTe, XSSFSheet sheetTr,
			List<String> keyList, Map<String, Object> voMap, int rowNo)
			throws Exception {

		XSSFCell cellTe;
		XSSFCell keyCellTe;
		XSSFCell cellTr;
		XSSFCell keyCellTr;

		XSSFRow rowListTe = sheetTe.createRow(rowNo);
		XSSFRow rowListTr = sheetTr.createRow(rowNo);
		rowNo++;

		keyCellTe = rowListTe.createCell(0);
		keyCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCellTe.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCellTe.setCellValue("-");

		keyCellTr = rowListTr.createCell(0);
		keyCellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCellTr.setCellStyle(cellStyleMap.get(trFilePath).get(0));
		keyCellTr.setCellValue("-");

		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);

			cellTe = rowListTe.createCell(i + 1);
			cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
			cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(4));
			cellTe.setCellValue(String.valueOf(voMap.get(key)));

			cellTr = rowListTr.createCell(i + 1);
			cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
			cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(4));
			cellTr.setCellValue(String.valueOf(voMap.get(key)));
		}

		return rowNo;
	}

	/**
	 * VOデータを作成する。
	 *
	 * @param sheetTe
	 *            TEファイルのシート
	 * @param sheetTr
	 *            TRファイルのシート
	 * @param pagingVO
	 *            ページングVOデータ
	 * @param rowNo
	 *            行No
	 * @return 行No
	 * @throws Exception
	 *             例外発生時
	 */
	private int writePagingVOData(XSSFSheet sheetTe, XSSFSheet sheetTr,
			PagingVO pagingVO, int rowNo) throws Exception {

		// 行の作成
		XSSFRow rowTe = sheetTe.createRow(rowNo);
		XSSFRow rowTr = sheetTr.createRow(rowNo);
		rowNo++;

		// ページングVOタイトルを作成する。
		writePagingVOTitle(teFilePath, rowTe);
		writePagingVOTitle(trFilePath, rowTr);

		// ページングVOデータを作成する。
		writePagingVODetailData(teFilePath, sheetTe, pagingVO, rowNo);
		rowNo = writePagingVODetailData(trFilePath, sheetTr, pagingVO, rowNo);

		return rowNo;
	}

	/**
	 * ページングVOタイトルを作成する。
	 *
	 * @param filePath
	 *            EXCELファイルパス
	 * @param titleRow
	 *            タイトル行
	 */
	private void writePagingVOTitle(String filePath, XSSFRow titleRow) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = titleRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("PagingVO");

		cell = titleRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(filePath).get(1));
		cell.setCellValue(PagingVO.class.getName());
	}

	/**
	 * ページングVOタイトルを作成する。
	 *
	 * @param wb
	 *            EXCELファイル
	 * @param titleRow
	 *            タイトル行
	 * @param pagingVO
	 *            ページングVOデータ
	 * @param rowNo
	 *            行No
	 * @return 行No
	 */
	private int writePagingVODetailData(String filePath, XSSFSheet sheet,
			PagingVO pagingVO, int rowNo) {

		XSSFRow row = sheet.createRow(rowNo);
		rowNo++;

		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = row.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("COLUMN");

		cell = row.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(filePath).get(2));
		cell.setCellValue("nowPageCount");

		cell = row.createCell(2);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(filePath).get(2));
		cell.setCellValue("allCount");

		cell = row.createCell(3);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(filePath).get(2));
		cell.setCellValue("pagingLimit");

		row = sheet.createRow(rowNo);
		rowNo++;

		keyCell = row.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCell.setCellValue("項目名");

		cell = row.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(filePath).get(3));
		cell.setCellValue("");

		cell = row.createCell(2);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(filePath).get(3));
		cell.setCellValue("");

		cell = row.createCell(3);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(filePath).get(3));
		cell.setCellValue("");

		XSSFCell cellTe;
		XSSFCell keyCellTe;

		row = sheet.createRow(rowNo);
		rowNo++;

		keyCellTe = row.createCell(0);
		keyCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCellTe.setCellStyle(cellStyleMap.get(filePath).get(0));
		keyCellTe.setCellValue("-");

		cellTe = row.createCell(1);
		cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		cellTe.setCellStyle(cellStyleMap.get(filePath).get(4));
		cellTe.setCellValue(pagingVO.getNowPageCount());

		cellTe = row.createCell(2);
		cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		cellTe.setCellStyle(cellStyleMap.get(filePath).get(4));
		cellTe.setCellValue(pagingVO.getAllCount());

		cellTe = row.createCell(3);
		cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
		cellTe.setCellStyle(cellStyleMap.get(filePath).get(4));
		cellTe.setCellValue(pagingVO.getPagingLimit());

		return rowNo;
	}

	/**
	 * 明細VOのデータを作成する。
	 *
	 * @param sheetTe
	 *            TEのシート
	 * @param sheetTr
	 *            TRのシート
	 * @param list
	 *            明細データ
	 * @param rowNo
	 *            行No
	 * @param detailClass
	 *            明細VOのクラス
	 * @return 行No
	 * @throws Exception
	 *             例外発生時
	 */
	private int writeDetailVOListData(XSSFSheet sheetTe, XSSFSheet sheetTr,
			List<?> list, int rowNo, Class<?> detailClass) throws Exception {

		XSSFCell cellTe;
		XSSFCell keyCellTe;
		XSSFCell cellTr;
		XSSFCell keyCellTr;

		int count = 0;
		for (Object cInstance : list) {
			count++;

			XSSFRow rowListTe = sheetTe.createRow(rowNo);
			XSSFRow rowListTr = sheetTr.createRow(rowNo);
			rowNo++;

			keyCellTe = rowListTe.createCell(0);
			keyCellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
			keyCellTe.setCellStyle(cellStyleMap.get(teFilePath).get(0));
			keyCellTe.setCellValue(count);

			keyCellTr = rowListTr.createCell(0);
			keyCellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
			keyCellTr.setCellStyle(cellStyleMap.get(trFilePath).get(0));
			keyCellTr.setCellValue(count);

			// 明細VOのメンバーの取得
			List<Field> fields = getAllFields(detailClass);

			int i = 0;
			for (Field field : fields) {

				// 明細VOメンバー名の取得
				String key = field.getName();
				// get方法名の取得
				String methodName = "";

				if (key.substring(1, 2).toUpperCase()
						.equals(key.substring(1, 2))) {
					methodName = "get" + key.substring(0, 1)
							+ key.substring(1, 2).toUpperCase()
							+ key.substring(2);
				} else {
					methodName = "get" + key.substring(0, 1).toUpperCase()
							+ key.substring(1);
				}
				if ("__cobertura_counters".equals(key)) {
					continue;
				}

				// get方法の取得
				Method m = detailClass.getMethod(methodName);

				if ("class java.util.Date".equals(m.getReturnType().toString())) {
					// get方法の行う
					Date value = (Date) m.invoke(cInstance);

					cellTe = rowListTe.createCell(i + 1);
					cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
					cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(4));
					cellTe.setCellValue((value == null) ? ""
							: (changeDateToString(value, "yyyy/MM/dd HH:mm:ss")));

					cellTr = rowListTr.createCell(i + 1);
					cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
					cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(4));
					cellTr.setCellValue((value == null) ? ""
							: (changeDateToString(value, "yyyy/MM/dd HH:mm:ss")));

					i++;

					continue;
				}

				if ("class java.sql.Timestamp".equals(m.getReturnType()
						.toString())) {
					// get方法の行う
					Timestamp value = (Timestamp) m.invoke(cInstance);

					cellTe = rowListTe.createCell(i + 1);
					cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
					cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(4));
					cellTe.setCellValue((value == null) ? "" : (value
							.toString()));

					cellTr = rowListTr.createCell(i + 1);
					cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
					cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(4));
					cellTr.setCellValue((value == null) ? "" : (value
							.toString()));

					i++;

					continue;
				}

				if ("String[]".equals(m.getReturnType().getSimpleName())) {
					// get方法の行う
					String[] value = (String[]) m.invoke(cInstance);

					cellTe = rowListTe.createCell(i + 1);
					cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
					cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(4));
					cellTe.setCellValue((value == null) ? ""
							: (changeStringArrayToString(value)));

					cellTr = rowListTr.createCell(i + 1);
					cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
					cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(4));
					cellTr.setCellValue((value == null) ? ""
							: (changeStringArrayToString(value)));

					i++;

					continue;
				}

				// get方法の行う
				Object value = m.invoke(cInstance);

				cellTe = rowListTe.createCell(i + 1);
				cellTe.setCellType(XSSFCell.CELL_TYPE_STRING);
				cellTe.setCellStyle(cellStyleMap.get(teFilePath).get(4));
				cellTe.setCellValue((value == null) ? "" : (String
						.valueOf(value)));

				cellTr = rowListTr.createCell(i + 1);
				cellTr.setCellType(XSSFCell.CELL_TYPE_STRING);
				cellTr.setCellStyle(cellStyleMap.get(trFilePath).get(4));
				cellTr.setCellValue((value == null) ? "" : (String
						.valueOf(value)));
				i++;
			}
		}

		return rowNo;
	}

	/**
	 * TEのシート「差分DB」を作成する。
	 *
	 * @param wb
	 *            ファイル
	 * @throws Exception
	 *             例外発生時
	 */
	private void createDifferentDBSheet(XSSFWorkbook wb) throws Exception {
		// シート「差分DB」の作成
		XSSFSheet sheet = wb.getSheet(TE_SHEET_NAME_5);

		int lineNo = 6;
		int tableNo = 0;
		for (String tableName : tableNames) {
			tableNo++;

			// 処理前DBデータ
			List<Map<String, Object>> motoDataList = new ArrayList<Map<String, Object>>();
			// 処理後DBデータ
			List<Map<String, Object>> lastDataList = new ArrayList<Map<String, Object>>();
			// 主キーリスト
			List<String> pKeyList = new ArrayList<String>();

			// テーブルの主キーの取得
			readPrimaryKey(pKeyList, tableName);
			// TEシート「処理前DB」の読む
			readTableData(wb, motoDataList, tableName, TE_SHEET_NAME_2);
			// TEシート「処理後DB」の読む
			readTableData(wb, lastDataList, tableName, TE_SHEET_NAME_3);

			// 主キーで、差分DBの作成
			lineNo = compareDataByPKey(wb, sheet, tableName, lineNo, tableNo,
					motoDataList, lastDataList, pKeyList);

			// ブランク行の作成
			sheet.createRow(lineNo);
			lineNo++;
		}

		// END行の作成
		XSSFRow lastRow = sheet.createRow(lineNo);
		XSSFCell lastCell = lastRow.createCell(0);
		lastCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		lastCell.setCellValue("END");
	}

	/**
	 * TEのシート「差分VO」を作成する。
	 *
	 * @param wb
	 *            ファイル
	 * @throws Exception
	 *             例外発生時
	 */
	private void createDifferentVoSheet(XSSFWorkbook wb) throws Exception {
		// シート「差分VO」の取得
		XSSFSheet sheet = wb.getSheet(TE_SHEET_NAME_7);

		// 処理前VO
		Map<String, Object> voMap = new HashMap<String, Object>();
		// 処理後VO
		Map<String, Object> voMapTemp = new HashMap<String, Object>();
		// 処理前ページングVO
		Map<String, Object> pagingVoMap = new HashMap<String, Object>();
		// 処理後ページングVO
		Map<String, Object> pagingVoMapTemp = new HashMap<String, Object>();
		// 処理前明細VO
		Map<String, List<Map<String, Object>>> detailVOMap = new HashMap<String, List<Map<String, Object>>>();
		// 処理後明細VO
		Map<String, List<Map<String, Object>>> detailVOMapTemp = new HashMap<String, List<Map<String, Object>>>();
		// 処理前VOキーリスト
		List<String> voKeyList = new ArrayList<String>();
		// 処理後VOキーリスト
		List<String> voKeyListTemp = new ArrayList<String>();
		// ページングVOキーリスト
		List<String> pagingVoKeyList = new ArrayList<String>();
		// 処理前明細VOキーリスト
		Map<String, List<String>> detailKeyMap = new HashMap<String, List<String>>();
		// 処理後明細VOキーリスト
		Map<String, List<String>> detailKeyMapTemp = new HashMap<String, List<String>>();

		// 処理前VOデータの取得
		String className = readVOData(wb, voMap, pagingVoMap, detailVOMap,
				voKeyList, detailKeyMap, TE_SHEET_NAME_4);
		// 処理後VOデータの取得
		readVOData(wb, voMapTemp, pagingVoMapTemp, detailVOMapTemp,
				voKeyListTemp, detailKeyMapTemp, TE_SHEET_NAME_6);

		// VOの差分データ
		Map<String, String[]> difVoMap = new HashMap<String, String[]>();
		// ページングVOの差分データ
		Map<String, String[]> difPagingVoMap = new HashMap<String, String[]>();
		// 明細VO削除されたデータ
		Map<String, List<Map<String, Object>>> delDetailVOMap = new HashMap<String, List<Map<String, Object>>>();
		// 明細VO追加されたデータ
		Map<String, List<Map<String, Object>>> insertDetailVOMap = new HashMap<String, List<Map<String, Object>>>();

		pagingVoKeyList.add(0, "nowPageCount");
		pagingVoKeyList.add(1, "allCount");
		pagingVoKeyList.add(2, "pagingLimit");

		// VOデータの比較を行う、差分VOデータの取得
		compVOData(voMap, voMapTemp, difVoMap, voKeyList);
		if (!pagingVoMap.keySet().isEmpty()) {
			// ページングVOデータの比較を行う、差分ページングVOデータの取得
			compVOData(pagingVoMap, pagingVoMapTemp, difPagingVoMap,
					pagingVoKeyList);
		}
		// 明細VOデータの比較を行う
		compDetaiVOData(detailVOMap, detailVOMapTemp, delDetailVOMap,
				insertDetailVOMap);

		int rowNum = 6;
		// 差分VOデータの作成
		rowNum = writeVODiffData(wb, sheet, difVoMap, voKeyList, className,
				rowNum);
		if (!pagingVoMap.keySet().isEmpty()) {
			// 差分ページングVOデータの作成
			rowNum = writeVODiffData(wb, sheet, difPagingVoMap,
					pagingVoKeyList, PagingVO.class.getName(), rowNum);
		}
		// 明細VO差分データの作成
		rowNum = writeDetailVODiffData(sheet, delDetailVOMap,
				insertDetailVOMap, detailKeyMapTemp, className, rowNum);

		// END行の作成
		XSSFRow lastRow = sheet.createRow(rowNum);
		XSSFCell lastCell = lastRow.createCell(0);
		lastCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		lastCell.setCellValue("END");
	}

	/**
	 * EXCELからテーブルデータを取得する。
	 *
	 * @param dataList
	 *            取得されたテーブルデータ。
	 * @param tableName
	 *            テーブル名
	 * @param sheetName
	 *            シート名
	 * @throws Exception
	 *             例外発生時
	 */
	private void readTableData(XSSFWorkbook wb,
			List<Map<String, Object>> dataList, String tableName,
			String sheetName) throws Exception {

		// EXCELシートの取得
		XSSFSheet sheet = wb.getSheet(sheetName);

		List<String> keyList = new ArrayList<String>();
		boolean readDataFlag = false;
		int count = 0;

		// シートの行数の取得
		int rows = sheet.getLastRowNum();

		for (int i = 0; i < rows; i++) {
			XSSFRow row = sheet.getRow(i);

			// ブランク行ではない場合
			if ((row != null) && (row.getCell(0) != null)) {
				// 第一列がTABLEの場合
				if ("TABLE".equals(row.getCell(0).toString())) {
					if ((tableName.toUpperCase()).equals(row.getCell(1)
							.getStringCellValue())) {
						// テーブルデータ読む
						readDataFlag = true;
					} else {
						// テーブルデータ読まない
						readDataFlag = false;
					}
				} else {
					// テーブルデータ読むの場合
					if (readDataFlag) {
						// 第一列がCOLUMNの場合
						if ("COLUMN".equals(row.getCell(0).toString())) {
							int columns = row.getLastCellNum();

							for (int j = 0; j < columns - 1; j++) {
								if ((row.getCell(j + 1) == null)
										|| (row.getCell(j + 1)
												.getStringCellValue() == null)
										|| ("".equals(row.getCell(j + 1)
												.getStringCellValue().trim()))) {
									break;
								}

								String key = row.getCell(j + 1)
										.getStringCellValue();

								keyList.add(j, key);
							}
						} else if (!"項目名".equals(row.getCell(0).toString())) {
							// 第一列が項目名以外の場合

							Map<String, Object> map = new HashMap<String, Object>();

							for (int k = 0; k < keyList.size(); k++) {
								String value = row.getCell(k + 1)
										.getStringCellValue();
								String key = keyList.get(k);

								map.put(key, value);
							}

							dataList.add(count, map);
							count++;
						}
					}
				}
			} else {
				// テーブルデータ読まない
				readDataFlag = false;
			}
		}

	}

	/**
	 * EXCELファイルから、VOデータの取得を行う。
	 *
	 * @param wb
	 *            EXCELファイル
	 * @param voMap
	 *            VOデータ
	 * @param pagingVoMap
	 *            ページングVOデータ
	 * @param detailVOMap
	 *            明細VOデータ
	 * @param voKeyList
	 *            VOキーリスト
	 * @param detailVOKeyMap
	 *            明細VOのメンバー名Map
	 * @param sheetName
	 *            EXCELファイルシート名
	 * @return　VOクラス名
	 */
	private String readVOData(XSSFWorkbook wb, Map<String, Object> voMap,
			Map<String, Object> pagingVoMap,
			Map<String, List<Map<String, Object>>> detailVOMap,
			List<String> voKeyList, Map<String, List<String>> detailVOKeyMap,
			String sheetName) throws Exception {
		// EXCELシートの取得
		XSSFSheet sheet = wb.getSheet(sheetName);

		boolean hasDetailFlag = false;
		String className = "";
		String detailParamName = "";

		List<String> detailKeyList = new ArrayList<String>();
		List<Map<String, Object>> detailVOList = new ArrayList<Map<String, Object>>();

		// シートの行数の取得
		int rows = sheet.getLastRowNum();

		for (int i = 6; i < rows; i++) {
			XSSFRow row = sheet.getRow(i);

			// ブランク行ではない場合
			if ((row != null) && (row.getCell(0) != null)) {

				row.getCell(0).setCellType(XSSFCell.CELL_TYPE_STRING);
				if ((row.getCell(0) != null)
						&& ("END".equals(row.getCell(0).getStringCellValue()))) {
					break;
				}

				// 第一列がVOの場合
				if ("VO".equals(row.getCell(0).toString())) {
					row.getCell(1).setCellType(XSSFCell.CELL_TYPE_STRING);
					className = row.getCell(1).getStringCellValue();

					// キーの行を取得
					XSSFRow keyRow = sheet.getRow(i + 1);
					// データの行を取得
					XSSFRow valueRow = sheet.getRow(i + 3);

					i = i + 3;

					// 列数の取得
					int columns = keyRow.getLastCellNum();

					for (int j = 0; j < columns - 1; j++) {
						if ((keyRow.getCell(j + 1) == null)
								|| (keyRow.getCell(j + 1).getStringCellValue() == null)
								|| ("".equals(keyRow.getCell(j + 1)
										.getStringCellValue().trim()))) {
							break;
						}

						// VOキーの取得
						keyRow.getCell(j + 1).setCellType(
								XSSFCell.CELL_TYPE_STRING);
						String key = keyRow.getCell(j + 1).getStringCellValue();

						// VO値の取得
						valueRow.getCell(j + 1).setCellType(
								XSSFCell.CELL_TYPE_STRING);
						String value = valueRow.getCell(j + 1)
								.getStringCellValue();

						// VOキーリストの取得
						voKeyList.add(j, key);

						// VOデータの取得
						voMap.put(key, value);
					}
				}

				// 第一列がPagingVOの場合
				if ("PagingVO".equals(row.getCell(0).toString())) {

					// キーの行を取得
					XSSFRow keyRow = sheet.getRow(i + 1);
					// データの行を取得
					XSSFRow valueRow = sheet.getRow(i + 3);

					i = i + 3;

					// 列数の取得
					int columns = keyRow.getLastCellNum();

					for (int j = 0; j < columns - 1; j++) {
						if ((keyRow.getCell(j + 1) == null)
								|| (keyRow.getCell(j + 1).getStringCellValue() == null)
								|| ("".equals(keyRow.getCell(j + 1)
										.getStringCellValue().trim()))) {
							break;
						}

						// PagingVOキーの取得
						keyRow.getCell(j + 1).setCellType(
								XSSFCell.CELL_TYPE_STRING);
						String key = keyRow.getCell(j + 1).getStringCellValue();

						// PagingVO値の取得
						valueRow.getCell(j + 1).setCellType(
								XSSFCell.CELL_TYPE_STRING);
						String value = valueRow.getCell(j + 1)
								.getStringCellValue();

						// PagingVOデータの取得
						pagingVoMap.put(key, value);
					}
				}

				// 第一列がDETAILで始める場合
				if (row.getCell(0).toString().startsWith("DETAIL")) {
					hasDetailFlag = true;

					detailKeyList = new ArrayList<String>();

					// 明細VOデータある場合
					if (detailVOList.size() > 0) {
						detailVOMap.put(detailParamName, detailVOList);
					}

					detailVOList = new ArrayList<Map<String, Object>>();

					if (row.getCell(1) == null) {
						throw new Exception("明細パラメータ指定してください。");
					}

					row.getCell(1).setCellType(XSSFCell.CELL_TYPE_STRING);
					// 明細VOデータのパラメータ名の取得
					detailParamName = row.getCell(1).getStringCellValue();

				} else if ("COLUMN".equals(row.getCell(0).toString())) {
					// 第一列がCOLUMN場合

					// 明細VOデータある場合
					if (hasDetailFlag) {
						int columns = row.getLastCellNum();

						for (int j = 0; j < columns - 1; j++) {
							if ((row.getCell(j + 1) == null)
									|| (row.getCell(j + 1).getStringCellValue() == null)
									|| ("".equals(row.getCell(j + 1)
											.getStringCellValue().trim()))) {
								break;
							}

							// 明細VOキーの取得
							row.getCell(j + 1).setCellType(
									XSSFCell.CELL_TYPE_STRING);
							String key = row.getCell(j + 1)
									.getStringCellValue();

							detailKeyList.add(j, key);

							detailVOKeyMap.put(detailParamName, detailKeyList);
						}
					}

				} else if (hasDetailFlag) {
					// 明細VOデータある場合

					if ("項目名".equals(row.getCell(0).toString())) {
						continue;
					}

					Map<String, Object> map = new HashMap<String, Object>();

					for (int j = 0; j < detailKeyList.size(); j++) {
						// 明細VOキーの取得
						String key = detailKeyList.get(j);

						// 明細VOデータの明細値の取得
						row.getCell(j + 1).setCellType(
								XSSFCell.CELL_TYPE_STRING);
						String value = row.getCell(j + 1).getStringCellValue();

						map.put(key, value);
					}

					detailVOList.add(map);
				}

			} else {
				hasDetailFlag = false;
			}
		}

		// 明細VOデータある場合
		if (detailVOList.size() > 0) {
			detailVOMap.put(detailParamName, detailVOList);
		}

		return className;
	}

	/**
	 * 主キーで、差分DBを作成する。
	 *
	 * @param wb
	 *            EXCELファイル
	 * @param sheet
	 *            EXCELシート
	 * @param tableName
	 *            テーブル名
	 * @param lineNo
	 *            行No
	 * @param tableNo
	 *            テーブルNo
	 * @param motoDataList
	 *            処理前テーブルデータ
	 * @param lastDataList
	 *            処理後テーブルデータ
	 * @param pKeyList
	 *            テーブル主キーのリスト
	 * @return 行No
	 * @throws Exception
	 *             例外発生時
	 */
	private int compareDataByPKey(XSSFWorkbook wb, XSSFSheet sheet,
			String tableName, int lineNo, int tableNo,
			List<Map<String, Object>> motoDataList,
			List<Map<String, Object>> lastDataList, List<String> pKeyList)
			throws Exception {
		// 削除されたデータの取得
		List<Map<String, Object>> delDataList = findDelOrInsertData(
				motoDataList, lastDataList, pKeyList);
		// 追加されたデータの取得
		List<Map<String, Object>> insertDataList = findDelOrInsertData(
				lastDataList, motoDataList, pKeyList);
		// 違ったデータの取得
		List<Map<String, String[]>> difDataList = findDiffData(motoDataList,
				lastDataList, pKeyList);
		// テーブルキーリスト
		List<String> keyList = new ArrayList<String>();

		XSSFCell cell;
		XSSFCell keyCell;

		XSSFRow headRow = sheet.createRow(lineNo);
		lineNo++;

		keyCell = headRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("TABLE");

		cell = headRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(teFilePath).get(1));
		cell.setCellValue(tableName.toUpperCase());

		cell = headRow.createCell(5);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(teFilePath).get(1));
		cell.setCellValue(tableNo);

		XSSFRow row = sheet.createRow(lineNo);
		lineNo++;
		XSSFRow rowComment = sheet.createRow(lineNo);
		lineNo++;

		keyCell = rowComment.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("項目名");

		keyCell = row.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("COLUMN");

		// テーブルのキーID、キー名とコメントの取得
		String sql = "";
		sql += "SELECT ";
		sql += "    COL.COLUMN_NAME AS COLUMN_NAME , ";
		sql += "    DES.DESCRIPTION AS COMMENTS ";
		sql += "FROM ";
		sql += "    INFORMATION_SCHEMA.COLUMNS COL LEFT JOIN PG_DESCRIPTION DES ";
		sql += "        ON COL.TABLE_NAME::REGCLASS = DES.OBJOID ";
		sql += "    AND COL.ORDINAL_POSITION = DES.OBJSUBID ";
		sql += "WHERE ";
		sql += "    TABLE_SCHEMA = 'yodo_tran' ";
		sql += "    AND TABLE_NAME = '" + tableName.toLowerCase() + "' ";
		sql += "ORDER BY ";
		sql += "    ORDINAL_POSITION; ";

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		int m = 0;

		while (rs.next()) {
			cell = row.createCell(m + 1);

			cell.setCellType(XSSFCell.CELL_TYPE_STRING);

			// 主キーの場合、文字が赤いで表示
			if (pKeyList.contains(rs.getString("COLUMN_NAME"))) {
				cell.setCellStyle(cellStyleMap.get(teFilePath).get(5));
			} else {
				cell.setCellStyle(cellStyleMap.get(teFilePath).get(2));
			}
			// テーブルキー名の表示
			cell.setCellValue(rs.getString("COLUMN_NAME"));

			cell = rowComment.createCell(m + 1);

			// 主キーの場合、文字が赤いで表示
			if (pKeyList.contains(rs.getString("COLUMN_NAME"))) {
				cell.setCellStyle(cellStyleMap.get(teFilePath).get(6));
			} else {
				cell.setCellStyle(cellStyleMap.get(teFilePath).get(3));
			}

			// テーブルコメントの表示
			cell.setCellValue(rs.getString("COMMENTS"));

			keyList.add(m, rs.getString("COLUMN_NAME"));
			m++;
		}

		// 削除されたデータの書く
		for (int j = 0; j < delDataList.size(); j++) {

			XSSFRow rowList = sheet.createRow(lineNo);
			lineNo++;

			XSSFCell firstCell = rowList.createCell(0);
			firstCell.setCellType(XSSFCell.CELL_TYPE_STRING);

			firstCell.setCellStyle(cellStyleMap.get(teFilePath).get(7));
			firstCell.setCellValue("-");

			for (int k = 0; k < keyList.size(); k++) {

				cell = rowList.createCell(k + 1);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);

				cell.setCellStyle(cellStyleMap.get(teFilePath).get(4));

				cell.setCellValue(String.valueOf(delDataList.get(j).get(
						keyList.get(k))));
			}

		}

		// 追加されたデータの書く
		for (int j = 0; j < insertDataList.size(); j++) {

			XSSFRow rowList = sheet.createRow(lineNo);
			lineNo++;

			XSSFCell firstCell = rowList.createCell(0);
			firstCell.setCellType(XSSFCell.CELL_TYPE_STRING);

			firstCell.setCellStyle(cellStyleMap.get(teFilePath).get(7));
			firstCell.setCellValue("+");

			for (int k = 0; k < keyList.size(); k++) {

				cell = rowList.createCell(k + 1);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);

				cell.setCellStyle(cellStyleMap.get(teFilePath).get(4));

				cell.setCellValue(String.valueOf(insertDataList.get(j).get(
						keyList.get(k))));
			}

		}

		// 違ったデータの書く
		for (int j = 0; j < difDataList.size(); j++) {

			XSSFRow rowList = sheet.createRow(lineNo);
			lineNo++;
			XSSFRow difRowList = sheet.createRow(lineNo);
			lineNo++;

			XSSFCell firstCell = rowList.createCell(0);
			firstCell.setCellType(XSSFCell.CELL_TYPE_STRING);

			firstCell.setCellStyle(cellStyleMap.get(teFilePath).get(7));
			firstCell.setCellValue("U");

			for (int k = 0; k < keyList.size(); k++) {

				cell = rowList.createCell(k + 1);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);

				cell.setCellStyle(cellStyleMap.get(teFilePath).get(4));

				String[] value = difDataList.get(j).get(keyList.get(k));

				cell.setCellValue(value[0]);

				if (!"DEFAULT_VALUE".equals(value[1])) {
					cell = difRowList.createCell(k + 1);

					cell.setCellType(XSSFCell.CELL_TYPE_STRING);

					cell.setCellStyle(cellStyleMap.get(teFilePath).get(8));

					cell.setCellValue(value[1]);
				}

			}
		}

		return lineNo;
	}

	/**
	 * 差分VOデータを取得する。
	 *
	 * @param voMap
	 *            処理前VOデータ
	 * @param voMapTemp
	 *            処理後VOデータ
	 * @param difVOMap
	 *            差分VOデータ
	 * @param voKeyList
	 *            VOキーリスト
	 */
	private void compVOData(Map<String, Object> voMap,
			Map<String, Object> voMapTemp, Map<String, String[]> difVOMap,
			List<String> voKeyList) {
		boolean hasDiff = false;
		Map<String, String[]> difVOMapTemp = new HashMap<String, String[]>();

		for (String key : voKeyList) {
			// 処理前VOメンバーデータの取得
			String value = (voMap.get(key) == null) ? "" : String.valueOf(voMap
					.get(key));
			// 処理後VOメンバーデータの取得
			String valueTemp = (voMapTemp.get(key) == null) ? "" : String
					.valueOf(voMapTemp.get(key));

			// 差分の取得
			String[] strArray = new String[2];
			if ((!value.equals(valueTemp))) {
				hasDiff = true;

				strArray[0] = value;

				strArray[1] = valueTemp;
			} else {
				strArray[0] = value;

				strArray[1] = null;
			}

			difVOMapTemp.put(key, strArray);

		}

		if (hasDiff) {
			for (String key : difVOMapTemp.keySet()) {
				difVOMap.put(key, difVOMapTemp.get(key));
			}
		}
	}

	/**
	 * 処理前と処理後明細VOデータを比較して、削除された明細VOデータと追加された明細VOデータを取得する。
	 *
	 * @param detailVOMap
	 *            処理前明細VOデータ
	 * @param detailVOMapTemp
	 *            処理後明細VOデータ
	 * @param delDetailVOMap
	 *            削除された明細VOデータ
	 * @param insertDetailVOMap
	 *            追加された明細VOデータ
	 */
	private void compDetaiVOData(
			Map<String, List<Map<String, Object>>> detailVOMap,
			Map<String, List<Map<String, Object>>> detailVOMapTemp,
			Map<String, List<Map<String, Object>>> delDetailVOMap,
			Map<String, List<Map<String, Object>>> insertDetailVOMap) {

		if (detailVOMap.keySet().isEmpty()) {
			for (String detailParamName : detailVOMapTemp.keySet()) {
				List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();

				insertList.addAll(detailVOMapTemp.get(detailParamName));

				if (insertList.size() > 0) {
					insertDetailVOMap.put(detailParamName, insertList);
				}
			}
		}

		for (String detailParamName : detailVOMap.keySet()) {
			List<Map<String, Object>> delList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();

			// 処理後明細VOデータなし場合
			if ((detailVOMapTemp.get(detailParamName) == null)
					|| (detailVOMapTemp.get(detailParamName).size() == 0)) {
				// 処理前明細VOデータある場合
				if ((detailVOMap.get(detailParamName) != null)
						&& (detailVOMap.get(detailParamName).size() > 0)) {
					delList.addAll(detailVOMap.get(detailParamName));
				}
			} else {
				// 処理後明細VOデータある場合

				// 処理前明細VOデータなし場合
				if ((detailVOMap.get(detailParamName) == null)
						|| (detailVOMap.get(detailParamName).size() == 0)) {
					insertList.addAll(detailVOMapTemp.get(detailParamName));
				} else {
					// 処理前明細VOデータある場合

					// 処理前明細VOデータの取得
					List<Map<String, Object>> list = detailVOMap
							.get(detailParamName);
					// 処理後明細VOデータの取得
					List<Map<String, Object>> listTemp = detailVOMapTemp
							.get(detailParamName);

					// 削除された明細VOデータの取得
					for (Map<String, Object> dataMap : list) {

						boolean hasEqualData = false;

						for (Map<String, Object> dataMapTemp : listTemp) {
							boolean everyKeyEqual = true;

							for (String key : dataMap.keySet()) {
								Object value = dataMap.get(key);
								Object valueTemp = dataMapTemp.get(key);

								if (!compareObjectData(value, valueTemp)) {
									everyKeyEqual = false;
									break;
								}
							}

							if (everyKeyEqual) {
								hasEqualData = true;
								break;
							}
						}

						if (!hasEqualData) {
							delList.add(dataMap);
						}

					}

					// 追加された明細VOデータの取得
					for (Map<String, Object> dataMapTemp : listTemp) {

						boolean hasEqualData = false;

						for (Map<String, Object> dataMap : list) {
							boolean everyKeyEqual = true;

							for (String key : dataMapTemp.keySet()) {
								if (!compareObjectData(dataMap.get(key),
										dataMapTemp.get(key))) {
									everyKeyEqual = false;
									break;
								}
							}

							if (everyKeyEqual) {
								hasEqualData = true;
								break;
							}
						}

						if (!hasEqualData) {
							insertList.add(dataMapTemp);
						}

					}

				}
			}

			if (delList.size() > 0) {
				delDetailVOMap.put(detailParamName, delList);
			}
			if (insertList.size() > 0) {
				insertDetailVOMap.put(detailParamName, insertList);
			}
		}

	}

	/**
	 * 差分VOデータを作成する。
	 *
	 * @param wb
	 *            excelファイル
	 * @param sheet
	 *            excelシート
	 * @param difVOMap
	 *            差分VOデータ
	 * @param voKeyList
	 *            VOキーリスト
	 * @param className
	 *            VOクラス名
	 * @param rowNum
	 *            行数
	 * @return 行数
	 */
	private int writeVODiffData(XSSFWorkbook wb, XSSFSheet sheet,
			Map<String, String[]> difVOMap, List<String> voKeyList,
			String className, int rowNum) {
		XSSFCell cell;
		XSSFCell keyCell;

		// タイトル行
		XSSFRow titleRow = sheet.createRow(rowNum);
		rowNum++;

		keyCell = titleRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		if (PagingVO.class.getName().equals(className)) {
			keyCell.setCellValue("PagingVO");
		} else {
			keyCell.setCellValue("VO");
		}

		cell = titleRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);

		cell.setCellStyle(cellStyleMap.get(teFilePath).get(1));
		cell.setCellValue(className);

		// ヘッダ行
		XSSFRow headRow = sheet.createRow(rowNum);
		rowNum++;

		keyCell = headRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("COLUMN");

		// VOキーの書く
		for (int i = 0; i < voKeyList.size(); i++) {
			cell = headRow.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(teFilePath).get(2));
			cell.setCellValue(voKeyList.get(i));
		}

		// コメント行
		XSSFRow commentRow = sheet.createRow(rowNum);
		rowNum++;

		keyCell = commentRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("項目名");

		// VOキーのコメントの書く
		for (int i = 0; i < voKeyList.size(); i++) {
			cell = commentRow.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(teFilePath).get(3));
			cell.setCellValue("");
		}

		if (difVOMap.keySet().isEmpty()) {
			sheet.createRow(rowNum);
			rowNum++;

			return rowNum;
		}

		// 処理前データ行
		XSSFRow rowListTe = sheet.createRow(rowNum);
		rowNum++;
		// 処理後データ行
		XSSFRow rowDiffTe = sheet.createRow(rowNum);
		rowNum++;

		XSSFCell firstCell = rowListTe.createCell(0);
		firstCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		firstCell.setCellStyle(cellStyleMap.get(teFilePath).get(7));
		firstCell.setCellValue("U");

		for (int i = 0; i < voKeyList.size(); i++) {
			String key = voKeyList.get(i);

			String[] value = difVOMap.get(key);

			cell = rowListTe.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(teFilePath).get(4));
			cell.setCellValue((value[0] == null ? "" : value[0]));

			if (value[1] != null) {
				cell = rowDiffTe.createCell(i + 1);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);

				cell.setCellStyle(cellStyleMap.get(teFilePath).get(8));
				cell.setCellValue(value[1]);
			}

		}

		sheet.createRow(rowNum);
		rowNum++;

		return rowNum;
	}

	/**
	 * TEファイルのシート「差分VO」を書く。
	 *
	 * @param sheet
	 *            excelシート
	 * @param delDetailVOMap
	 *            削除された明細VOデータ
	 * @param insertDetailVOMap
	 *            追加された明細VOデータ
	 * @param detailVOKeyMap
	 *            明細VOのキー
	 * @param className
	 *            VOクラス名
	 * @param rowNum
	 *            行数
	 * @return 行数
	 * @throws Exception
	 *             例外発生時
	 */
	private int writeDetailVODiffData(XSSFSheet sheet,
			Map<String, List<Map<String, Object>>> delDetailVOMap,
			Map<String, List<Map<String, Object>>> insertDetailVOMap,
			Map<String, List<String>> detailVOKeyMap, String className,
			int rowNum) throws Exception {

		int detailNum = 0;
		List<String> paraNameList = new ArrayList<String>();

		// 削除された明細VOデータのメンバー名をメンバー名リストに追加する
		for (String key : delDetailVOMap.keySet()) {
			if (!paraNameList.contains(key)) {
				paraNameList.add(key);
			}
		}

		// 追加された明細VOデータのメンバー名をメンバー名リストに追加する
		for (String key : insertDetailVOMap.keySet()) {
			if (!paraNameList.contains(key)) {
				paraNameList.add(key);
			}
		}

		for (String paraName : paraNameList) {
			List<Map<String, Object>> delDetailVODataList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> insertDetailVODataList = new ArrayList<Map<String, Object>>();

			// 削除されたデータの取得
			if (delDetailVOMap.containsKey(paraName)) {
				delDetailVODataList = delDetailVOMap.get(paraName);
			}

			// 追加されたデータの取得
			if (insertDetailVOMap.containsKey(paraName)) {
				insertDetailVODataList = insertDetailVOMap.get(paraName);
			}

			List<String> keyList = new ArrayList<String>();

			if (((delDetailVODataList != null) && (delDetailVODataList.size() > 0))
					|| ((insertDetailVODataList != null) && (insertDetailVODataList
							.size() > 0))) {
				detailNum++;

				XSSFRow titleRow = sheet.createRow(rowNum);
				rowNum++;

				// TEファイルのシート「差分VO」で、明細VOデータ差分タイトル部の書く
				writeDetailVODiffTitleData(titleRow, className, paraName,
						detailNum);

				// 明細VOデータキーリストの取得
				keyList = detailVOKeyMap.get(paraName);

				XSSFRow headRow = sheet.createRow(rowNum);
				rowNum++;

				// TEファイルのシート「差分VO」で、明細VOデータ差分ヘッダ部の書く
				writeDetailVODiffHeadData(headRow, keyList);

				XSSFRow commentRow = sheet.createRow(rowNum);
				rowNum++;

				// TEファイルのシート「差分VO」で、明細VOデータ差分コメント部の書く
				writeDetailVODiffCommentData(commentRow, keyList);
			}

			// 削除されたデータの書く
			if ((delDetailVODataList != null)
					&& (delDetailVODataList.size() > 0)) {
				rowNum = writeDetailVODiffListData(sheet, delDetailVODataList,
						keyList, rowNum, true);
			}

			// 追加されたデータの書く
			if ((insertDetailVODataList != null)
					&& (insertDetailVODataList.size() > 0)) {
				rowNum = writeDetailVODiffListData(sheet,
						insertDetailVODataList, keyList, rowNum, false);
			}
			sheet.createRow(rowNum);
			rowNum++;
		}

		return rowNum;
	}

	/**
	 * 明細VOデータ差分タイトル部を書く。
	 *
	 * @param titleRow
	 *            タイトル行
	 * @param className
	 *            VOクラス名
	 * @param paraName
	 *            明細VOのメンバー名
	 * @param detailNum
	 *            明細No
	 * @throws Exception
	 *             例外発生時
	 */
	private void writeDetailVODiffTitleData(XSSFRow titleRow, String className,
			String paraName, int detailNum) throws Exception {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = titleRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);

		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("DETAIL");

		Class<?> modelClass = Class.forName(className);
		Field field = getCurField(modelClass, paraName);
		String paraClassName = field.getGenericType().toString();
		paraClassName = paraClassName.substring(paraClassName.indexOf("<") + 1,
				paraClassName.lastIndexOf(">"));

		cell = titleRow.createCell(1);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(teFilePath).get(1));
		cell.setCellValue(paraName);

		cell = titleRow.createCell(2);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyleMap.get(teFilePath).get(1));
		cell.setCellValue(paraClassName);
	}

	/**
	 * 明細VOデータ差分ヘッダ部を書く。
	 *
	 * @param headRow
	 *            ヘッダ行
	 * @param keyList
	 *            明細VOのキーリスト
	 */
	private void writeDetailVODiffHeadData(XSSFRow headRow, List<String> keyList) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = headRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("COLUMN");

		int i = 0;

		for (String key : keyList) {

			cell = headRow.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(teFilePath).get(2));
			cell.setCellValue(key);

			i++;
		}
	}

	/**
	 * 明細VOデータ差分コメント部を書く。
	 *
	 * @param headRow
	 *            ヘッダ行
	 * @param keyList
	 *            明細VOのキーリスト
	 */
	private void writeDetailVODiffCommentData(XSSFRow headRow,
			List<String> keyList) {
		XSSFCell cell;
		XSSFCell keyCell;

		keyCell = headRow.createCell(0);
		keyCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		keyCell.setCellStyle(cellStyleMap.get(teFilePath).get(0));
		keyCell.setCellValue("項目名");

		for (int i = 0; i < keyList.size(); i++) {
			cell = headRow.createCell(i + 1);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleMap.get(teFilePath).get(3));
			cell.setCellValue("");
		}
	}

	/**
	 * 　削除と追加された明細VOデータを書く。
	 *
	 * @param sheet
	 *            excelシート
	 * @param detailVODataList
	 *            明細VOデータ
	 * @param keyList
	 *            明細VOデータのキーリスト
	 * @param rowNum
	 *            行数
	 * @param processType
	 *            true:削除の場合 false:追加の場合
	 * @return 行数
	 */
	private int writeDetailVODiffListData(XSSFSheet sheet,
			List<Map<String, Object>> detailVODataList, List<String> keyList,
			int rowNum, boolean processType) {
		XSSFCell cell;

		for (Map<String, Object> detailData : detailVODataList) {

			XSSFRow rowList = sheet.createRow(rowNum);
			rowNum++;

			XSSFCell firstCell = rowList.createCell(0);
			firstCell.setCellType(XSSFCell.CELL_TYPE_STRING);

			firstCell.setCellStyle(cellStyleMap.get(teFilePath).get(7));

			// 削除の場合
			if (processType) {
				firstCell.setCellValue("-");
			} else {
				firstCell.setCellValue("+");
			}

			int i = 0;
			for (String key : keyList) {
				cell = rowList.createCell(i + 1);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyleMap.get(teFilePath).get(4));
				cell.setCellValue(String.valueOf(detailData.get(key)));

				i++;
			}
		}

		return rowNum;
	}

	/**
	 * 削除されたデータ　または　追加されたデータを取得する。
	 *
	 * @param motoDataList
	 *            処理前データ　または　処理後データ
	 * @param lastDataList
	 *            処理後データ　または　処理前データ
	 * @param pKeyList
	 *            主キーデータ
	 * @return 削除されたデータ　または　追加されたデータ
	 */
	private List<Map<String, Object>> findDelOrInsertData(
			List<Map<String, Object>> motoDataList,
			List<Map<String, Object>> lastDataList, List<String> pKeyList) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> motoData : motoDataList) {
			boolean result = false;

			for (Map<String, Object> lastData : lastDataList) {
				if (compareKey(motoData, lastData, pKeyList)) {
					result = true;
					break;
				}
			}

			if (!result) {
				dataList.add(motoData);
			}
		}

		return dataList;
	}

	/**
	 * 違ったデータを取得する。
	 *
	 * @param motoDataList
	 *            処理前データ
	 * @param lastDataList
	 *            処理後データ
	 * @param pKeyList
	 *            主キーリスト
	 * @return 違ったデータ
	 */
	private List<Map<String, String[]>> findDiffData(
			List<Map<String, Object>> motoDataList,
			List<Map<String, Object>> lastDataList, List<String> pKeyList) {
		List<Map<String, String[]>> difDataList = new ArrayList<Map<String, String[]>>();
		int count = 0;
		for (Map<String, Object> motoData : motoDataList) {
			boolean result = false;

			for (Map<String, Object> lastData : lastDataList) {
				if (compareKey(motoData, lastData, pKeyList)) {

					Map<String, String[]> difData = new HashMap<String, String[]>();

					for (String key : motoData.keySet()) {
						String[] value = new String[2];

						value[0] = motoData.get(key).toString();
						value[1] = "DEFAULT_VALUE";

						if (!(motoData.get(key)).equals(lastData.get(key))) {
							value[1] = lastData.get(key).toString();
							result = true;
						}

						difData.put(key, value);
					}

					if (result) {
						difDataList.add(count, difData);
						count++;
					}

					break;
				}
			}
		}

		return difDataList;
	}

	/**
	 * VOデータを取得する。
	 *
	 * @param c
	 *            VOクラス
	 * @param cInstance
	 *            VO対象
	 * @param voMap
	 *            　VOデータ
	 * @param pagingVoMap
	 *            ページングVOデータ
	 * @param detailVOMap
	 *            　明細VOデータ
	 * @throws Exception
	 *             例外発生時
	 */
	private void getVoData(Class<?> c, Object cInstance,
			Map<String, Object> voMap, Map<String, Object> pagingVoMap,
			Map<String, List<Map<String, Object>>> detailVOMap)
			throws Exception {

		for (String key : voMap.keySet()) {

			if ("reports".equals(key)) {
				continue;
			}

			String setMethod = "";

			if (key.substring(1, 2).toUpperCase().equals(key.substring(1, 2))) {
				setMethod = "set" + key.substring(0, 1)
						+ key.substring(1, 2).toUpperCase() + key.substring(2);
			} else {
				setMethod = "set" + key.substring(0, 1).toUpperCase()
						+ key.substring(1);
			}

			Field keyField = getCurField(c, key);

			if (key.equals("pcCommonTBLJiKouteiSlit1to24")) {
				System.out.println(keyField);
			}

			Class<?> keyType = keyField.getType();

			Method m = c.getMethod(setMethod, keyType);

			// VOデータをVOにセットする
			if ("int".equals(keyType.toString())) {
				m.invoke(cInstance,
						Integer.valueOf(String.valueOf(voMap.get(key))));
			} else if ("class java.math.BigDecimal".equals(keyType.toString())) {
				if (!StringUtils.isBlank(String.valueOf(voMap.get(key)))) {
					m.invoke(cInstance,
							new BigDecimal(String.valueOf(voMap.get(key))));
				}
			} else if ("class java.util.Date".equals(keyType.toString())) {
				m.invoke(
						cInstance,
						((voMap.get(key) == null) || ("".equals(String
								.valueOf(voMap.get(key))))) ? null
								: changeStringToDate(
										String.valueOf(voMap.get(key)),
										"yyyy/MM/dd HH:mm:ss"));
			} else if ("class java.sql.Timestamp".equals(keyType.toString())) {
				m.invoke(cInstance, ((voMap.get(key) == null) || (""
						.equals(String.valueOf(voMap.get(key))))) ? null
						: Timestamp.valueOf(String.valueOf(voMap.get(key))));
			} else if ("class java.lang.Long".equals(keyType.toString())) {
				m.invoke(cInstance, new Long(String.valueOf(voMap.get(key))));
			} else if ("String[]".equals(keyType.getSimpleName())) {
				Object value = new Object();
				if (StringUtils.isBlank(String.valueOf(voMap.get(key)))) {
					value = null;
				} else {
					value = String.valueOf(voMap.get(key)).split(",");
				}

				m.invoke(cInstance, value);
			} else if ("Integer".equals(keyType.getSimpleName())) {
				if (StringUtils.isNotBlank(String.valueOf(voMap.get(key)))) {
					m.invoke(cInstance,
							Integer.valueOf(String.valueOf(voMap.get(key))));

				}
			} else if ("byte[]".equals(keyType.getSimpleName())) {
				Object value = new Object();
				if (StringUtils.isBlank(String.valueOf(voMap.get(key)))) {
					value = null;
				} else {
					value = String.valueOf(voMap.get(key)).split(",");
				}

				m.invoke(cInstance, value);
			} else if ("BigDecimal[]".equals(keyType.getSimpleName())) {

				if (StringUtils.isBlank(String.valueOf(voMap.get(key)))) {

				} else {

					String[] tempValue = String.valueOf(voMap.get(key)).split(
							",");
					BigDecimal[] value = new BigDecimal[tempValue.length];

					for (int i = 0; i < tempValue.length; i++) {

						if (!tempValue[i].equals("null")) {
							value[i] = new BigDecimal(tempValue[i]);
						} else {
							value[i] = null;
						}
					}

					m.invoke(cInstance, (Object) value);
				}
			} else {
				m.invoke(cInstance, keyType.cast(voMap.get(key)));
			}
		}

		if (!pagingVoMap.keySet().isEmpty()) {
			PagingVO pagingVO = new PagingVO();

			pagingVO.setNowPageCount((pagingVoMap.get("nowPageCount") == null) ? ""
					: String.valueOf(pagingVoMap.get("nowPageCount")));
			pagingVO.setAllCount((pagingVoMap.get("allCount") == null) ? ""
					: String.valueOf(pagingVoMap.get("allCount")));
			pagingVO.setPagingLimit((pagingVoMap.get("pagingLimit") == null) ? ""
					: String.valueOf(pagingVoMap.get("pagingLimit")));

			Method m = c.getMethod("setPagingVO", PagingVO.class);

			// ページングVOデータをVOにセットする
			m.invoke(cInstance, pagingVO);
		}

		for (String detailName : detailVOMap.keySet()) {
			List<Object> detail = new ArrayList<Object>();

			Field field = getCurField(c, detailName);
			String listTotalType = field.getGenericType().toString();

			// 明細VO対象のタイプを取得する。
			Class<?> detailClass = Class
					.forName(listTotalType.substring(
							listTotalType.indexOf("<") + 1,
							listTotalType.indexOf(">")));

			// 明細VOデータリストを取得する
			List<Map<String, Object>> detailVOList = detailVOMap
					.get(detailName);

			for (Map<String, Object> detailData : detailVOList) {
				// 明細VO対象の作成
				Object detailInstance = detailClass.newInstance();

				for (String key : detailData.keySet()) {
					// set方法名前
					String setMethod = "";

					if (key.substring(1, 2).toUpperCase()
							.equals(key.substring(1, 2))) {
						setMethod = "set" + key.substring(0, 1)
								+ key.substring(1, 2).toUpperCase()
								+ key.substring(2);
					} else {
						setMethod = "set" + key.substring(0, 1).toUpperCase()
								+ key.substring(1);
					}

					Method[] methods = detailClass.getMethods();
					for (Method m : methods) {
						// set方法の取得
						if (setMethod.equals(m.getName())) {
							Class<?>[] paraType = m.getParameterTypes();

							// 明細VOのメンバーがINTの場合
							if ("int".equals(paraType[0].toString())) {
								// set方法の実行
								m.invoke(detailInstance, Integer.valueOf(String
										.valueOf(detailData.get(key))));
							} else if ("class java.math.BigDecimal"
									.equals(paraType[0].toString())) {

								if (!StringUtils.isBlank(String
										.valueOf(detailData.get(key)))) {
									m.invoke(
											detailInstance,
											new BigDecimal(String
													.valueOf(detailData
															.get(key))));
								}
							} else if ("class java.util.Date"
									.equals(paraType[0].toString())) {
								m.invoke(
										detailInstance,
										((detailData.get(key) == null) || (""
												.equals(String
														.valueOf(detailData
																.get(key))))) ? null
												: changeStringToDate(String
														.valueOf(detailData
																.get(key)),
														"yyyy/MM/dd HH:mm:ss"));
							} else if ("class java.sql.Timestamp"
									.equals(paraType[0].toString())) {
								m.invoke(
										detailInstance,
										((detailData.get(key) == null) || (""
												.equals(String
														.valueOf(detailData
																.get(key))))) ? null
												: Timestamp.valueOf(String
														.valueOf(detailData
																.get(key))));
							} else if ("class java.lang.Long"
									.equals(paraType[0].toString())) {
								m.invoke(
										detailInstance,
										new Long(String.valueOf(detailData
												.get(key))));
							} else if ("String[]".equals(paraType[0]
									.getSimpleName())) {
								Object value = new Object();
								if (StringUtils.isBlank(String
										.valueOf(detailData.get(key)))) {
									value = null;
								} else {
									value = String.valueOf(detailData.get(key))
											.split(",");
								}

								m.invoke(detailInstance, value);
							} else if ("List".equals(paraType[0]
									.getSimpleName())) {
							} else {
								// set方法の実行
								m.invoke(detailInstance,
										paraType[0].cast(detailData.get(key)));
							}

							break;
						}
					}
				}

				detail.add(detailInstance);
			}

			String setMethod = "set" + detailName.substring(0, 1).toUpperCase()
					+ detailName.substring(1);
			// 明細VOデータをVOにセットする
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				if (setMethod.equals(m.getName())) {
					m.invoke(cInstance, detail);
					break;
				}
			}
		}
	}

	/**
	 * 期待VOデータと実の結果を比較する。
	 *
	 * @param c
	 *            メンバークラス
	 * @param fields
	 *            全部のメンバー
	 * @param cInstance
	 *            実の結果
	 * @param cInstanceTemp
	 *            期待の結果
	 * @throws Exception
	 *             例外発生時
	 */
	private void compareVoAllFields(Class<?> c, List<Field> fields,
			Object cInstance, Object cInstanceTemp) throws Exception {

		for (Field field : fields) {

			if ((field.getName()).equals("outputTime")) {
				continue;
			}
			if ((field.getName()).equals("date")) {
				continue;
			}
			if ((field.getName()).equals("outDateTime")) {
				continue;
			}
			if ((field.getName()).equals("repDatetimeHidden")) {
				continue;
			}
			if ((field.getName()).equals("hashCode")) {
				continue;
			}

			String key = field.getName();

			String methodName = "";

			if (key.substring(1, 2).toUpperCase().equals(key.substring(1, 2))) {
				methodName = "get" + key.substring(0, 1)
						+ key.substring(1, 2).toUpperCase() + key.substring(2);
			} else {
				methodName = "get" + key.substring(0, 1).toUpperCase()
						+ key.substring(1);
			}

			if ("__cobertura_counters".equals(key)) {
				continue;
			}

			Method m = c.getMethod(methodName);

			if ("pagingVO".equals(key)) {
				PagingVO pagingVO = (PagingVO) m.invoke(cInstance);
				PagingVO pagingVOTemp = (PagingVO) m.invoke(cInstanceTemp);

				if (!compareStringData(pagingVO.getNowPageCount(),
						pagingVOTemp.getNowPageCount())) {
					throw new Exception(c.toString()
							+ "のメンバーpagingVOのメンバーnowPageCountの値不具合。" + "実際値："
							+ pagingVO.getNowPageCount() + "  期待値："
							+ pagingVOTemp.getNowPageCount());
				}

				if (!compareStringData(pagingVO.getAllCount(),
						pagingVOTemp.getAllCount())) {
					throw new Exception(c.toString()
							+ "のメンバーpagingVOのメンバーallCountの値不具合。" + "実際値："
							+ pagingVO.getAllCount() + "  期待値："
							+ pagingVOTemp.getAllCount());
				}

				if (!compareStringData(pagingVO.getPagingLimit(),
						pagingVOTemp.getPagingLimit())) {
					throw new Exception(c.toString()
							+ "のメンバーpagingVOのメンバーpagingLimitの値不具合。" + "実際値："
							+ pagingVO.getPagingLimit() + "  期待値："
							+ pagingVOTemp.getPagingLimit());
				}

				continue;
			}

			Object value = m.invoke(cInstance);
			Object valueTemp = m.invoke(cInstanceTemp);

			if ("interface java.util.List".equals(field.getType().toString())) {
				String listTotalType = field.getGenericType().toString();
				// 明細対象のタイプを取得する。
				Class<?> detailClass = Class.forName(listTotalType.substring(
						listTotalType.indexOf("<") + 1,
						listTotalType.indexOf(">")));

				List<Field> detailFields = getAllFields(detailClass);

				List<?> detail = (List<?>) value;
				List<?> detailTemp = (List<?>) valueTemp;

				for (Object detailDataTemp : detailTemp) {
					boolean checkResult = false;
					for (Object detailData : detail) {

						try {
							compareVoAllFields(detailClass, detailFields,
									detailData, detailDataTemp);
						} catch (Exception e) {
							continue;
						}

						checkResult = true;
						break;
					}

					if (!checkResult) {
						throw new Exception(c.toString() + "のメンバー" + key
								+ "期待値不具合。" + "期待値："
								+ detailDataTemp.toString());
					}

				}

			} else {
				if ("String[]".equals(field.getType().getSimpleName())) {
					value = changeStringArrayToString((String[]) value);
					valueTemp = changeStringArrayToString((String[]) valueTemp);
				}
				if ("BigDecimal[]".equals(field.getType().getSimpleName())) {
					value = changeBigDecimalArrayToString((BigDecimal[]) value);
					valueTemp = changeBigDecimalArrayToString((BigDecimal[]) valueTemp);
				}

				if (!compareObjectData(value, valueTemp)) {
					throw new Exception(c.toString() + "のメンバー" + key + "の値不具合。"
							+ "実際値：" + value + "  期待値：" + valueTemp);
				}
			}

		}
	}

	/**
	 * TR期待DBデータと実行後データの比較を行う。
	 *
	 * @throws Exception
	 *             例外発生時
	 */
	private void compareTrDbResultData() throws Exception {
		XSSFWorkbook wbTe = new XSSFWorkbook(new FileInputStream(trFilePath));

		for (String tableName : tableNames) {
			List<String> keyList = new ArrayList<String>();
			List<String> pKeyList = new ArrayList<String>();
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> curDataList = new ArrayList<Map<String, Object>>();
			List<String> timeStampKey = new ArrayList<String>();
			// 見落とすフィールド
			List<String> ignoreStr = new ArrayList<String>();
			// 採番ハッシュコード
			ignoreStr.add("saiban_hash_cd");
			if (tableName.contains("jiseki")) {
				ignoreStr.add("seihin_syousy_no");
			}
			if (tableName.contains("tbl_t_kotei_plan")) {
				ignoreStr.add("genban_syouyaku_no");
			}

			readPrimaryKey(pKeyList, tableName);

			// TRシート「期待DB」にDBデータを取得する。
			readTableData(wbTe, dataList, tableName, TR_SHEET_NAME_4);

			// データベースから、実行後データを取得する。
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM "
					+ tableName);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				List<String> keyListTemp = new ArrayList<String>();
				Map<String, Object> rowMap = new HashMap<String, Object>();

				int columnCount = rsmd.getColumnCount();
				for (int j = 0; j < columnCount; j++) {

					// テーブルのキーを取得する。
					keyListTemp.add(j, rsmd.getColumnName(j + 1));

					// データベースに、キーがDATEの場合
					if ("date".equals(rsmd.getColumnTypeName(j + 1)
							.toLowerCase())) {
						Date value = rs.getDate(rsmd.getColumnName(j + 1));

						if (!timeStampKey.contains(keyListTemp.get(j))) {
							timeStampKey.add(keyListTemp.get(j));
						}

						if (value != null) {
							rowMap.put(
									keyListTemp.get(j),
									changeDateToString(value,
											"yyyy/MM/dd HH:mm:ss"));
						} else {
							rowMap.put(keyListTemp.get(j), "");
						}
					} else if ("timestamp".equals(rsmd.getColumnTypeName(j + 1)
							.toLowerCase())) {
						Timestamp value = rs.getTimestamp(rsmd
								.getColumnName(j + 1));

						if (!timeStampKey.contains(keyListTemp.get(j))) {
							timeStampKey.add(keyListTemp.get(j));
						}

						if (value != null) {
							rowMap.put(
									keyListTemp.get(j),
									changeDateToString(value,
											"yyyy/MM/dd HH:mm:ss.SSS"));
						} else {
							rowMap.put(keyListTemp.get(j), "");
						}
					} else {
						String value = rs.getString(rsmd.getColumnName(j + 1));

						if ("".equals(value)) {
							rowMap.put(keyListTemp.get(j), "''");
						} else {
							rowMap.put(keyListTemp.get(j), (value == null ? ""
									: value));
						}

					}
				}

				if (keyList.size() == 0) {
					keyList.addAll(keyListTemp);
				}

				curDataList.add(rowMap);
			}

			for (Map<String, Object> tableData : dataList) {
				boolean hasDataFlag = false;

				for (Map<String, Object> curTableData : curDataList) {
					boolean pkeyEqualFlag = true;

					for (String pkey : pKeyList) {
						if (!compareObjectData(tableData.get(pkey),
								curTableData.get(pkey))) {
							pkeyEqualFlag = false;
							break;
						}
					}

					if (pkeyEqualFlag) {
						hasDataFlag = true;

						for (String key : keyList) {

							if (timeStampKey.contains(key)
									|| ignoreStr.contains(key)) {
								continue;
							}

							if (!pKeyList.contains(key)) {
								if (!compareObjectData(tableData.get(key),
										curTableData.get(key))) {
									String msg = "";
									msg += "テーブル「" + tableName + "」のデータ不具合。";
									msg += "（";
									msg += "primary key={";
									for (int i = 0; i < pKeyList.size(); i++) {
										String pKey = pKeyList.get(i);

										if (i == pKeyList.size() - 1) {
											msg += pKey + "="
													+ tableData.get(pKey) + " ";
										} else {
											msg += pKey + "="
													+ tableData.get(pKey)
													+ ", ";
										}
									}
									msg += "} ";
									msg += "不具合メンバー：" + key + " ";
									msg += "実際値：" + curTableData.get(key) + " ";
									msg += "期待値：" + tableData.get(key) + " ";
									msg += "）";

									throw new Exception(msg);
								}
							}

						}

						break;
					}

				}

				if (!hasDataFlag) {
					String msg = "";
					msg += "テーブル「" + tableName + "」に、";
					msg += "primary key={";
					for (int i = 0; i < pKeyList.size(); i++) {
						String pKey = pKeyList.get(i);

						if (i == pKeyList.size() - 1) {
							msg += pKey + "=" + tableData.get(pKey) + " ";
						} else {
							msg += pKey + "=" + tableData.get(pKey) + ", ";
						}
					}
					msg += "} ";
					msg += "のデータがない。";

					throw new Exception(msg);
				}

			}

			for (Map<String, Object> curTableData : curDataList) {
				boolean hasDataFlag = false;

				for (Map<String, Object> tableData : dataList) {
					boolean pkeyEqualFlag = true;

					for (String pkey : pKeyList) {
						if (!compareObjectData(tableData.get(pkey),
								curTableData.get(pkey))) {
							pkeyEqualFlag = false;
							break;
						}
					}

					if (pkeyEqualFlag) {
						hasDataFlag = true;
						break;
					}
				}

				if (!hasDataFlag) {
					String msg = "";
					msg += "テーブル「" + tableName + "」の期待データに、";
					msg += "primary key={";
					for (int i = 0; i < pKeyList.size(); i++) {
						String pKey = pKeyList.get(i);

						if (i == pKeyList.size() - 1) {
							msg += pKey + "=" + curTableData.get(pKey) + " ";
						} else {
							msg += pKey + "=" + curTableData.get(pKey) + ", ";
						}
					}
					msg += "} ";
					msg += "のデータがない。";

					throw new Exception(msg);
				}

			}

		}
	}

	/**
	 * 二つ値の比較を行う。
	 *
	 * @param value
	 *            　値
	 * @param valueTemp
	 *            　ターゲット値
	 * @return　比較された結果
	 */
	private boolean compareObjectData(Object value, Object valueTemp) {
		if (((value == null) && (valueTemp != null))
				|| ((value != null) && (!value.equals(valueTemp)))) {
			return false;
		}

		return true;
	}

	/**
	 * 二つ値の比較を行う。
	 *
	 * @param value
	 *            　値
	 * @param valueTemp
	 *            　ターゲット値
	 * @return　比較された結果
	 */
	private boolean compareStringData(String value, String valueTemp) {
		if (((value == null) && (valueTemp != null))
				|| ((value != null) && (!value.equals(valueTemp)))) {
			return false;
		}

		return true;
	}

	/**
	 * 　処理前データと処理後データの整合性をチェックする。
	 *
	 * @param motoData
	 *            motoDataList 処理前データ　または　処理後データ
	 * @param lastData
	 *            処理後データ　または　処理前データ
	 * @param pKeyList
	 *            主キーデータ
	 * @return true:キーが同じ false:キーが同じではない
	 */
	private boolean compareKey(Map<String, Object> motoData,
			Map<String, Object> lastData, List<String> pKeyList) {
		boolean result = true;

		for (String key : pKeyList) {
			if (!(motoData.get(key)).equals(lastData.get(key))) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * テーブルの主キーを取得する。
	 *
	 * @param pKeyList
	 *            主キーリスト
	 * @param tableName
	 *            テーブル名
	 * @throws Exception
	 *             例外発生時
	 */
	private void readPrimaryKey(List<String> pKeyList, String tableName)
			throws Exception {
		Statement stmt = conn.createStatement();

		String sql = "";
		sql += "SELECT ";
		sql += "PG_CONSTRAINT.CONNAME AS PK_NAME, ";
		sql += "PG_ATTRIBUTE.ATTNAME AS COLNAME, ";
		sql += "PG_TYPE.TYPNAME AS TYPENAME ";
		sql += "FROM ";
		sql += "PG_CONSTRAINT ";
		sql += "INNER JOIN PG_CLASS ON PG_CONSTRAINT.CONRELID = PG_CLASS.OID ";
		sql += "INNER JOIN PG_ATTRIBUTE ON PG_ATTRIBUTE.ATTRELID = PG_CLASS.OID  AND  PG_ATTRIBUTE.ATTNUM = ANY (PG_CONSTRAINT.CONKEY) ";
		sql += "INNER JOIN PG_TYPE ON PG_TYPE.OID = PG_ATTRIBUTE.ATTTYPID ";
		sql += "WHERE PG_CLASS.RELNAME = '" + tableName.toLowerCase() + "' ";
		sql += "AND PG_CONSTRAINT.CONTYPE='p' ";
		sql += "AND PG_TABLE_IS_VISIBLE(PG_CLASS.OID) ";

		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			pKeyList.add(rs.getString("COLNAME"));
		}
	}

	/**
	 * Date型の日付をString変換する。
	 *
	 * @param date
	 *            変換したい日付
	 * @param format
	 *            変換形式(例:"yyyyMMdd","yyMMdd")
	 * @return 変換後の日付
	 */
	private String changeDateToString(Date date, String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * String型の日付をDate変換する。
	 *
	 * @param date
	 *            変換したい日付
	 * @param format
	 *            変換形式(例:"yyyyMMdd","yyMMdd")
	 * @return 変換後の日付
	 * @throws ParseException
	 */
	public static Date changeStringToDate(String date, String format)
			throws ParseException {

		DateFormat fomat = new SimpleDateFormat(format);
		fomat.setLenient(false);
		return fomat.parse(date);
	}

	/**
	 * パラメータタイプを取得する。
	 *
	 * @param classType
	 *            　クラスタイプ
	 * @param paraName
	 *            パラメータ名前
	 * @return パラメータのタイプ
	 */
	private Class<?> getParamType(Class<?> classType, String paraName) {
		Class<?> result;

		try {
			result = classType.getDeclaredField(paraName).getType();
		} catch (NoSuchFieldException e) {
			Class<?> superClass = classType.getSuperclass();
			result = getParamType(superClass, paraName);
		}
		return result;
	}

	/**
	 * クラス全部のフィールドを取得する。
	 *
	 * @param classType
	 *            　クラスタイプ
	 * @return　クラス全部のフィールド
	 */
	private List<Field> getAllFields(Class<?> classType) {
		List<Field> fields = new ArrayList<Field>();

		Field[] fieldsTemp = classType.getDeclaredFields();

		for (Field field : fieldsTemp) {
			// 静態の場合
			if ("private static final".equals(Modifier.toString(field
					.getModifiers()))) {
				continue;
			}

			if ("__cobertura_counters".equals(field.getName())) {
				continue;
			}

			fields.add(field);
		}

		if (classType.getSuperclass() != null) {
			fields.addAll(getAllFields(classType.getSuperclass()));
		}

		return fields;
	}

	/**
	 * フィールドを取得する。
	 *
	 * @param classType
	 *            　クラスタイプ
	 * @param fieldName
	 *            フィールド名
	 * @return　今のフィールド
	 */
	private Field getCurField(Class<?> classType, String fieldName) {
		Field[] fields = classType.getDeclaredFields();

		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		if (classType.getSuperclass() != null) {
			return getCurField(classType.getSuperclass(), fieldName);
		} else {
			return null;
		}
	}

	/**
	 * ファイルパス存在チェックして、ないの場合、作成する。
	 *
	 * @param filePath
	 *            ファイルパス
	 */
	private void pathCheck(String filePath) {
		File file = new File(filePath);
		// ファイルディレクトリの取得
		File parentFile = file.getParentFile();

		// ファイルディレクトリ存在ではない場合
		if (!parentFile.exists()) {
			// ディレクトリの作成
			parentFile.mkdirs();
		}
	}

	private String changeBigDecimalArrayToString(BigDecimal[] array) {
		String result = "";

		if (array == null) {
			return "";
		}

		for (int i = 0; i < array.length; i++) {
			BigDecimal str = array[i];

			if (i != 0) {
				result += ",";
			}

			result += str;
		}

		return result;
	}

	private String changeStringArrayToString(String[] array) {
		String result = "";

		if (array == null) {
			return "";
		}

		for (int i = 0; i < array.length; i++) {
			String str = array[i];

			if (i != 0) {
				result += ",";
			}

			result += str;
		}

		return result;
	}

	private void createCellStyle(XSSFWorkbook wbTe, XSSFWorkbook wbTr) {
		List<XSSFCellStyle> cellStyleListTe = new ArrayList<XSSFCellStyle>();

		XSSFCellStyle cellStyleForKeyTe = wbTe.createCellStyle();
		cellStyleForKeyTe.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyleForKeyTe
				.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyleForKeyTe.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleForKeyTe.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleForKeyTe.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleForKeyTe.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTe.add(0, cellStyleForKeyTe);

		XSSFCellStyle cellStyleForTitleTe = wbTe.createCellStyle();
		XSSFFont fontForTitleTe = wbTe.createFont();
		fontForTitleTe.setColor(HSSFColor.BLUE.index);
		cellStyleForTitleTe.setFont(fontForTitleTe);
		cellStyleListTe.add(1, cellStyleForTitleTe);

		XSSFCellStyle keyStyleTe = wbTe.createCellStyle();
		keyStyleTe.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		keyStyleTe.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		keyStyleTe.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		keyStyleTe.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		keyStyleTe.setBorderRight(XSSFCellStyle.BORDER_THIN);
		keyStyleTe.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTe.add(2, keyStyleTe);

		XSSFCellStyle commentStyleTe = wbTe.createCellStyle();
		commentStyleTe.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		commentStyleTe.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		commentStyleTe.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		commentStyleTe.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		commentStyleTe.setBorderRight(XSSFCellStyle.BORDER_THIN);
		commentStyleTe.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTe.add(3, commentStyleTe);

		XSSFCellStyle valueStyleTe = wbTe.createCellStyle();
		valueStyleTe.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		valueStyleTe.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		valueStyleTe.setBorderRight(XSSFCellStyle.BORDER_THIN);
		valueStyleTe.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTe.add(4, valueStyleTe);

		XSSFCellStyle cellStyleForColomnPkeyTe = wbTe.createCellStyle();
		cellStyleForColomnPkeyTe.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyleForColomnPkeyTe
				.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		cellStyleForColomnPkeyTe.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleForColomnPkeyTe.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleForColomnPkeyTe.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleForColomnPkeyTe.setBorderTop(XSSFCellStyle.BORDER_THIN);
		XSSFFont fontForPkeyTe = wbTe.createFont();
		fontForPkeyTe.setColor(HSSFColor.RED.index);
		cellStyleForColomnPkeyTe.setFont(fontForPkeyTe);
		cellStyleListTe.add(5, cellStyleForColomnPkeyTe);

		XSSFCellStyle cellStyleForCommentPkeyTe = wbTe.createCellStyle();
		cellStyleForCommentPkeyTe
				.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyleForCommentPkeyTe
				.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyleForCommentPkeyTe.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTe.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTe.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTe.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTe.setFont(fontForPkeyTe);
		cellStyleListTe.add(6, cellStyleForCommentPkeyTe);

		XSSFCellStyle cellStyleForDiffKeyCellTe = wbTe.createCellStyle();
		cellStyleForDiffKeyCellTe.setFont(fontForPkeyTe);
		cellStyleForDiffKeyCellTe.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		cellStyleListTe.add(7, cellStyleForDiffKeyCellTe);

		XSSFCellStyle cellStyleForDiffValCellTe = wbTe.createCellStyle();
		cellStyleForDiffValCellTe.setFont(fontForPkeyTe);
		cellStyleForDiffValCellTe.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleForDiffValCellTe.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleForDiffValCellTe.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleForDiffValCellTe.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTe.add(8, cellStyleForDiffValCellTe);

		cellStyleMap.put(teFilePath, cellStyleListTe);

		List<XSSFCellStyle> cellStyleListTr = new ArrayList<XSSFCellStyle>();

		XSSFCellStyle cellStyleForKeyTr = wbTr.createCellStyle();
		cellStyleForKeyTr.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyleForKeyTr
				.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyleForKeyTr.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleForKeyTr.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleForKeyTr.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleForKeyTr.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTr.add(0, cellStyleForKeyTr);

		XSSFCellStyle cellStyleForTitleTr = wbTr.createCellStyle();
		XSSFFont fontForTitleTr = wbTr.createFont();
		fontForTitleTr.setColor(HSSFColor.BLUE.index);
		cellStyleForTitleTr.setFont(fontForTitleTr);
		cellStyleListTr.add(1, cellStyleForTitleTr);

		XSSFCellStyle keyStyleTr = wbTr.createCellStyle();
		keyStyleTr.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		keyStyleTr.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		keyStyleTr.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		keyStyleTr.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		keyStyleTr.setBorderRight(XSSFCellStyle.BORDER_THIN);
		keyStyleTr.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTr.add(2, keyStyleTr);

		XSSFCellStyle commentStyleTr = wbTr.createCellStyle();
		commentStyleTr.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		commentStyleTr.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		commentStyleTr.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		commentStyleTr.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		commentStyleTr.setBorderRight(XSSFCellStyle.BORDER_THIN);
		commentStyleTr.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTr.add(3, commentStyleTr);

		XSSFCellStyle valueStyleTr = wbTr.createCellStyle();
		valueStyleTr.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		valueStyleTr.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		valueStyleTr.setBorderRight(XSSFCellStyle.BORDER_THIN);
		valueStyleTr.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleListTr.add(4, valueStyleTr);

		XSSFCellStyle cellStyleForColomnPkeyTr = wbTr.createCellStyle();
		cellStyleForColomnPkeyTr.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyleForColomnPkeyTr
				.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		cellStyleForColomnPkeyTr.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleForColomnPkeyTr.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleForColomnPkeyTr.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleForColomnPkeyTr.setBorderTop(XSSFCellStyle.BORDER_THIN);
		XSSFFont fontForPkeyTr = wbTr.createFont();
		fontForPkeyTr.setColor(HSSFColor.RED.index);
		cellStyleForColomnPkeyTr.setFont(fontForPkeyTr);
		cellStyleListTr.add(5, cellStyleForColomnPkeyTr);

		XSSFCellStyle cellStyleForCommentPkeyTr = wbTr.createCellStyle();
		cellStyleForCommentPkeyTr
				.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyleForCommentPkeyTr
				.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyleForCommentPkeyTr.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTr.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTr.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTr.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleForCommentPkeyTr.setFont(fontForPkeyTr);
		cellStyleListTr.add(6, cellStyleForCommentPkeyTr);

		cellStyleMap.put(trFilePath, cellStyleListTr);
	}
}
