package com.example.audiobookhub.data

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ZipHandler {

    fun zip(filesToCompress: List<String>, outputZipFilePath: String) {
        val buffer = ByteArray(1024)

        try {
            val fos = FileOutputStream(outputZipFilePath)
            val zos = ZipOutputStream(fos)

            filesToCompress.forEach { file ->
                val ze = ZipEntry(File(file).name)
                zos.putNextEntry(ze)
                val `in` = FileInputStream(file)
                while(true) {
                    val len = `in`.read(buffer)
                    if (len <= 0) break
                    zos.write(buffer, 0, len)
                }

                `in`.close()
            }

            zos.closeEntry()
            zos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unZip(
        zipFilePath: String,
        unzipDirectoryPath: String
    ) {
        val unzipDir = File(unzipDirectoryPath)
        if (!unzipDir.exists()) {
            unzipDir.mkdir()
        }

        val zipFile = File(zipFilePath)
        ZipFile(zipFile).unzip(unzipDir)
    }

    private fun ZipFile.unzip(
        unzipDir: File
    ) {
        val enum = entries()
        while (enum.hasMoreElements()) {
            Log.d("ZipHandler", "Unzipping")

            val entry = enum.nextElement()
            val entryName = entry.name
            val fis = FileInputStream(this.name)
            val zis = ZipInputStream(fis)

            while (true) {
                val nextEntry = zis.nextEntry ?: break
                if (nextEntry.name == entryName) {
                    val fout = FileOutputStream(File(unzipDir, nextEntry.name))
                    var c = zis.read()
                    while (c != -1) {
                        fout.write(c)
                        c = zis.read()
                    }
                    zis.closeEntry()
                    fout.close()
                }
            }

            zis.close()
        }
    }
}