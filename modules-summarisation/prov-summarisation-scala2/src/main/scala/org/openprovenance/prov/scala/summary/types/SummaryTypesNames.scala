package org.openprovenance.prov.scala.summary.types



trait SummaryTypesNames {
	final val RESOURCE="Resource"
	final val ENT = "Ent"
	final val ACT = "Act"
	final val AG = "Ag"
	final val PLAN = "Plan"
	final val PRIM = "Prim"
	final val WGB = "Wgb"
	final val USD = "Usd"
	final val WDF = "Wdf"
	final val WDF_triangle = "WdfT"
	final val WSB = "Wsb"
	final val WEB = "Web"
	final val WINVB = "Winvb"
	final val WAW = "Waw"
	final val WAT = "Wat"
	final val AOBO = "Aobo"
	final val WINFLB = "Winflb"
	final val WINFOB = "Winfob"
	final val ALT = "Alt"
	final val SPEC = "Spec"
	final val MEM = "Mem"
	final val MEN = "Men"
	final val PROVSET = "ProvSet"
	final val PROVBAG = "ProvBag"
	final val WGB_1 = "Wgb_1"
	final val USD_1 = "Usd_1"
	final val WDF_1 = "Wdf_1"
	final val WDF_triangle_1 = "WdfT_1"
	final val WSB_1 = "Wsb_1"
	final val WEB_1 = "Web_1"
	final val WINVB_1 = "Winvb_1"
	final val WAW_1 = "Waw_1"
	final val WAT_1 = "Wat_1"
	final val AOBO_1 = "Aobo_1"
	final val WINFLB_1 = "Winflb_1"
	final val WINFOB_1 = "Winfob_1"
	final val ALT_1 = "Alt_1"
	final val SPEC_1 = "Spec_1"
	final val MEM_1 = "Mem_1"
	final val MEN_1 = "Men_1"

	final val name_order_map: Map[String, Int] = {
		val order =new SummaryTypesOrder{}
		Map(RESOURCE -> order.RESOURCE,
			ENT -> order.ENT,
			ACT -> order.ACT,
			AG -> order.AG,
			//PLAN -> order.PLAN,
			PRIM -> order.PRIM,
			WGB -> order.WGB,
			USD -> order.USD,
			WDF -> order.WDF,
			WDF_triangle -> order.WDF_triangle,
			WSB -> order.WSB,
			WEB -> order.WEB,
			WINVB -> order.WINVB,
			WAW -> order.WAW,
			WAT -> order.WAT,
			AOBO -> order.AOBO,
			WINFLB -> order.WINFLB,
			WINFOB -> order.WINFOB,
			ALT -> order.ALT,
			SPEC -> order.SPEC,
			MEM -> order.MEM,
			MEN -> order.MEN,

			WGB_1 -> order.WGB_1,
			USD_1 -> order.USD_1,
			WDF_1 -> order.WDF_1,
			WSB_1 -> order.WSB_1,
			WEB_1 -> order.WEB_1,
			WINVB_1 -> order.WINVB_1,
			WAW_1 -> order.WAW_1,
			WAT_1 -> order.WAT_1,
			AOBO_1 -> order.AOBO_1,
			WINFLB_1 -> order.WINFLB_1,
			WINFOB_1 -> order.WINFOB_1,
			ALT_1 -> order.ALT_1,
			SPEC_1 -> order.SPEC_1,
			MEM_1 -> order.MEM_1,
			MEN_1 -> order.MEN_1)
	}

}
