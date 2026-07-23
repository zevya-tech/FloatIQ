//package com.harish.floatiq.ui.ai.formula
//
//object FormulaSearchEngine {
//
//    fun search(
//        query: String
//    ): List<FormulaInfo> {
//
//        if (query.isBlank()) {
//
//            return FormulaDatabase.formulas
//        }
//
//        return FormulaDatabase.formulas.filter {
//
//            it.name.contains(
//                query,
//                ignoreCase = true
//            )
//
//                    ||
//
//                    it.keywords.any { keyword ->
//
//                        keyword.contains(
//                            query,
//                            ignoreCase = true
//                        )
//                    }
//        }
//    }
//}
package com.harish.floatiq.ui.ai.formula

object FormulaSearchEngine {

    fun search(
        query: String,
        category: FormulaCategory?
    ): List<FormulaInfo> {

        var results =
            FormulaDatabase.formulas

        if (category != null) {

            results =
                results.filter {

                    it.category == category
                }
        }

        if (query.isBlank()) {

            return results
        }

        return results.filter {

            it.name.contains(
                query,
                ignoreCase = true
            )

                    ||

                    it.keywords.any {

                        it.contains(
                            query,
                            ignoreCase = true
                        )
                    }
        }
    }
}