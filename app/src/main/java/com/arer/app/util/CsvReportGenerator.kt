package com.arer.app.util

import android.content.Context
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.SchoolProfileEntity
import com.arer.app.domain.model.Money
import com.arer.app.domain.model.MonthlyMdmCalculationResult
import java.io.File
import java.io.FileWriter

object CsvReportGenerator {

    fun generateCsv(
        context: Context,
        result: MonthlyMdmCalculationResult,
        school: SchoolProfileEntity?,
        hm: HMProfileEntity?
    ): File {
        val file = File(context.cacheDir, "MDM_Report_${result.yearMonth}.csv")
        val writer = FileWriter(file)

        writer.append("ARER APP - MONTHLY MDM REPORT\n")
        writer.append("School Name,${school?.schoolName ?: "N/A"}\n")
        writer.append("UDISE Code,${school?.udiseCode ?: "N/A"}\n")
        writer.append("Report Period,${result.yearMonth}\n")
        writer.append("Working Days,${result.totalWorkingDays}\n")
        writer.append("MDM Days,${result.totalMdmDays}\n")
        writer.append("Students Served,${result.totalStudentsServed}\n\n")

        writer.append("Sl. No.,Item (English),Item (Kannada),Calculation Type,Rate (₹),Quantity,Total Amount (₹)\n")

        var sl = 1
        for (item in result.itemResults) {
            writer.append("$sl,")
            writer.append("\"${item.englishName}\",")
            writer.append("\"${item.kannadaName}\",")
            writer.append("${item.calculationType},")
            writer.append("${Money(item.ratePaise).toRupees()},")
            writer.append("${item.applicableQuantity},")
            writer.append("${Money(item.amountPaise).toRupees()}\n")
            sl++
        }

        writer.append("\n,,,,,Grand Total,${Money(result.grandTotalPaise).toRupees()}\n")
        writer.append("\nHead Master,${hm?.hmName ?: "N/A"}\n")
        writer.flush()
        writer.close()

        return file
    }
}
