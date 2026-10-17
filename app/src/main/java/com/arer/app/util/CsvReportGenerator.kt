package com.arer.app.util

import android.content.Context
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.SchoolProfileEntity
import com.arer.app.domain.model.Money
import com.arer.app.domain.model.MonthlyMdmCalculationResult
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

object CsvReportGenerator {

    fun generateCsv(
        context: Context,
        result: MonthlyMdmCalculationResult,
        school: SchoolProfileEntity?,
        hm: HMProfileEntity?
    ): File {
        val file = File(context.cacheDir, "MDM_Report_${result.yearMonth}.csv")
        val outputStream = FileOutputStream(file)

        // Write UTF-8 Byte Order Mark (\uFEFF -> 0xEF, 0xBB, 0xBF) for Microsoft Excel compatibility
        outputStream.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))

        val writer = OutputStreamWriter(outputStream, StandardCharsets.UTF_8)

        writer.write("\"ARER APP — MONTHLY MDM REPORT\"\n\n")
        writer.write("\"School Name:\",\"${escapeCsv(school?.schoolName ?: "N/A")}\"\n")
        writer.write("\"School Code:\",\"${escapeCsv(school?.schoolCode ?: "N/A")}\",\"UDISE Code:\",\"${escapeCsv(school?.udiseCode ?: "N/A")}\"\n")
        writer.write("\"District:\",\"${escapeCsv(school?.district ?: "N/A")}\",\"Taluk:\",\"${escapeCsv(school?.taluk ?: "N/A")}\"\n")
        writer.write("\"Head Master:\",\"${escapeCsv(hm?.hmName ?: "N/A")}\",\"Designation:\",\"${escapeCsv(hm?.designation ?: "Head Master")}\"\n")
        writer.write("\"Report Period:\",\"${escapeCsv(result.yearMonth)}\"\n")
        writer.write("\"Total Working Days:\",\"${result.totalWorkingDays}\",\"Total MDM Days:\",\"${result.totalMdmDays}\"\n")
        writer.write("\"Total Students Served:\",\"${result.totalStudentsServed}\"\n\n")

        writer.write("\"Sl. No.\",\"Item Name (English)\",\"Item Name (ಕನ್ನಡ)\",\"Calculation Basis\",\"Rate (₹)\",\"Quantity\",\"Total Amount (₹)\"\n")

        var sl = 1
        for (item in result.itemResults) {
            val rateFormatted = Money(item.ratePaise).toRupees()
            val totalFormatted = Money(item.amountPaise).toRupees()
            writer.write("\"$sl\",\"${escapeCsv(item.englishName)}\",\"${escapeCsv(item.kannadaName)}\",\"${item.calculationType}\",\"$rateFormatted\",\"${item.applicableQuantity}\",\"$totalFormatted\"\n")
            sl++
        }

        val grandTotalFormatted = Money(result.grandTotalPaise).formatInRupees()
        writer.write("\n\"\",\"\",\"\",\"\",\"\",\"Grand Total (ಒಟ್ಟು ಮೊತ್ತ):\",\"$grandTotalFormatted\"\n\n")
        writer.write("\"Head Master Signature:\",\"_______________________\",\"Date:\",\"\"\n")

        writer.flush()
        writer.close()

        return file
    }

    private fun escapeCsv(value: String): String {
        return value.replace("\"", "\"\"")
    }
}
