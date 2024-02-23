package com.example.audiobookhub.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.audiobookhub.R
import com.example.audiobookhub.data.model.Audio
import com.example.audiobookhub.data.model.AudioBook
import com.example.audiobookhub.data.model.Chapter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    @ApplicationContext val context: Context
) {

    private var books = listOf<AudioBook>()

    init {
        val bookFile = File(context.getExternalFilesDir(""), "books")
        if (!bookFile.exists()) {
            bookFile.mkdir()
        }
        val ogienPrzebudzenia = File(bookFile, "ogien_przebudzenia")
        if (!ogienPrzebudzenia.exists()) {
            ogienPrzebudzenia.createNewFile()

            val fileWriter = ogienPrzebudzenia.bufferedWriter()
            fileWriter.write("ogien_przebudzenia\n") // chapterFolderName
            fileWriter.write("Ogień Przebudzenia\n") // title
            fileWriter.write("Anthony Ryan\n") // author
            fileWriter.write("Joanna Domanska\n") // narrator
            fileWriter.write("0\n") // score
            fileWriter.write("0\n") // progress
            fileWriter.write("1.0\n") // playbackSpeed
            fileWriter.write("\n")
            fileWriter.write(
                "Na rozległych terytoriach kontrolowanych przez Żelazny Syndykat Handlowy najcenniejszym towarem jest smocza krew. Spuszczana z trzymanych w niewoli lub odławianych w dziczy Czerwonych, Zielonych, Niebieskich i Czarnych, po przedestylowaniu służy do wytwarzania eliksirów dających niewiarygodną moc. Tych, którzy potrafią z niej korzystać, nazywa się Błogosławionymi.\n" +
                        "Mało kto zna jednak prawdę: smocze rody słabną, a gdy wygasną całkowicie, wojna z sąsiednim Cesarstwem Corvuskim będzie nieunikniona. Ostatnią nadzieją Syndykatu są pogłoski o istnieniu innej rasy smoków, znacznie potężniejszej od pozostałych. Nieliczni wybrańcy losu udają się na jej poszukiwania.\n" +
                        "Claydon Torcreek – drobny złodziejaszek i niezarejestrowany Błogosławiony – po wcieleniu do służby w Protektoracie zostaje wysłany w głąb dzikich, niezbadanych krain w poszukiwaniu stworzenia rodem z legend. Lizanne Lethridge – kobieta-szpieg i znakomita zabójczyni – podczas misji na terytorium wroga musi stawić czoło wielkiemu zagrożeniu. Podporucznik Corrick Hilemore służy na krążowniku Syndykatu, który w pogoni za okrutnymi rozbójnikami na odległych rubieżach napotyka inne, znacznie gorsze niebezpieczeństwo. Kiedy żywoty ludzi i państw spotykają się i przeplatają, a to, co znane, zderza się z nieznanym, wszyscy troje muszą dołożyć wszelkich starań, żeby powstrzymać nadciągającą wojnę, która w przeciwnym razie ich pochłonie."
            )
            fileWriter.close()
        }

        val bookFolder = File(context.getExternalFilesDir(""), "ogien_przebudzenia")
        if (!bookFolder.exists()) {
            bookFolder.mkdir()
        }

        val ogien_przebudzenia_raw = listOf<InputStream>(
            context.resources.openRawResource(R.raw.ogien_przebudzenia_001),
            context.resources.openRawResource(R.raw.ogien_przebudzenia_002),
            context.resources.openRawResource(R.raw.ogien_przebudzenia_003),
        )

        val ogien_przebudzenia_output = listOf<FileOutputStream>(
            FileOutputStream(File(bookFolder, "ogien_przebudzenia_001.mp3")),
            FileOutputStream(File(bookFolder, "ogien_przebudzenia_002.mp3")),
            FileOutputStream(File(bookFolder, "ogien_przebudzenia_003.mp3")),
        )

        copyInputStreamToFile(ogien_przebudzenia_raw[0], ogien_przebudzenia_output[0])
        copyInputStreamToFile(ogien_przebudzenia_raw[1], ogien_przebudzenia_output[1])
        copyInputStreamToFile(ogien_przebudzenia_raw[2], ogien_przebudzenia_output[2])
    }

    init {
        val bookFile = File(context.getExternalFilesDir(""), "books")
        val mountain_of_madness = File(bookFile, "mountain_of_madness")
        if (!mountain_of_madness.exists()) {
            mountain_of_madness.createNewFile()

            val fileWriter = mountain_of_madness.bufferedWriter()
            fileWriter.write("mountain_of_madness\n") // chapterFolderName
            fileWriter.write("At the Mountains of Madness, Version 2\n") // title
            fileWriter.write("H. P. Lovecraft\n") // author
            fileWriter.write("Mark Nelson\n") // narrator
            fileWriter.write("0\n") // score
            fileWriter.write("0\n") // progress
            fileWriter.write("1.0\n") // playbackSpeed
            fileWriter.write("\n")
            fileWriter.write(
                "A scientific expedition to Antartica discovers the ruins of a civilization millions of years old, built by the Elder Things, beings who came to Earth shortly after the formation of the Moon. But are the ancient Elder Things extinct, or do they still roam the Earth? A classic tale of horror by H.P. Lovecraft that first appeared in Astounding Stories in 1936. - Summary by The Narrator"
            )
            fileWriter.close()
        }

        val bookFolder = File(context.getExternalFilesDir(""), "mountain_of_madness")
        if (!bookFolder.exists()) {
            bookFolder.mkdir()
        }

        val mountain_of_madness_raw = listOf<InputStream>(
            context.resources.openRawResource(R.raw.atthemountainsofmadness_01_lovecraft_64kb),
            context.resources.openRawResource(R.raw.atthemountainsofmadness_02_lovecraft_64kb),
            context.resources.openRawResource(R.raw.atthemountainsofmadness_03_lovecraft_64kb),
        )

        val mountain_of_madness_output = listOf<FileOutputStream>(
            FileOutputStream(File(bookFolder, "atthemountainsofmadness_01_lovecraft_64kb.mp3")),
            FileOutputStream(File(bookFolder, "atthemountainsofmadness_02_lovecraft_64kb.mp3")),
            FileOutputStream(File(bookFolder, "atthemountainsofmadness_03_lovecraft_64kb.mp3")),
        )

        copyInputStreamToFile(mountain_of_madness_raw[0], mountain_of_madness_output[0])
        copyInputStreamToFile(mountain_of_madness_raw[1], mountain_of_madness_output[1])
        copyInputStreamToFile(mountain_of_madness_raw[2], mountain_of_madness_output[2])
    }

    private fun copyInputStreamToFile(inputStream: InputStream, outputStream: FileOutputStream) {
        val buffer = ByteArray(8192)
        inputStream.use { input ->
            outputStream.use { fileOut ->

                while (true) {
                    val length = input.read(buffer)
                    if (length <= 0)
                        break
                    fileOut.write(buffer, 0, length)
                }
                fileOut.flush()
                fileOut.close()
            }
        }
        inputStream.close()
    }

    fun getBooks(): List<AudioBook> {
        val bookFolder = File(context.getExternalFilesDir(""), "books")
        val books = mutableListOf<AudioBook>()

        bookFolder.walk().forEach {
            if (it.isFile) {
                val book = getBook(it)
                books.add(book)
            }
        }

        this.books = books

        return books
    }

    fun saveBookState(book: AudioBook) {
        val bookData = File(context.getExternalFilesDir(""), "books/${book.chapterFolderName}")
        val fileWriter = bookData.bufferedWriter()
        fileWriter.write("${book.chapterFolderName}\n")
        fileWriter.write("${book.title}\n")
        fileWriter.write("${book.author}\n")
        fileWriter.write("${book.narrator}\n")
        fileWriter.write("${book.score}\n")
        fileWriter.write("${book.progress}\n")
        fileWriter.write("${book.playbackSpeed}\n")
        fileWriter.write("\n")
        fileWriter.write("${book.description}\n")
        fileWriter.close()
    }

    private fun getBook(bookData: File): AudioBook {
        val fileReader = bookData.bufferedReader()
        val chapterFolderName = fileReader.readLine()
        val title = fileReader.readLine()
        val author = fileReader.readLine()
        val narrator = fileReader.readLine()
        val score = fileReader.readLine().toInt()
        val progress = fileReader.readLine().toFloat()
        val playbackSpeed = fileReader.readLine().toFloat()

        val stringBuilder = StringBuilder()
        var line: String?
        while (fileReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }

        val description = stringBuilder.toString()

        val chapterFolder = File(context.getExternalFilesDir(""), chapterFolderName)

        val chapters = getBookChapters(chapterFolder)
        val duration = getBookDuration(chapters)
        val cover = if (chapters.isNotEmpty()) getCover(chapters[0]) else Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        val book = AudioBook(
            chapterFolderName,
            title,
            author,
            narrator,
            score,
            cover,
            duration,
            progress,
            playbackSpeed,
            description,
            chapters
        )

        return book
    }

    private fun getBookChapters(bookFolder: File): List<Chapter> {
        val chapters = mutableListOf<Chapter>()

        val metadataRetriever = MediaMetadataRetriever()
        var i = 1

        bookFolder.walk().forEach {
            if (it.isFile && it.extension == "mp3") {
                metadataRetriever.setDataSource(it.absolutePath)
                val duration =
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

                val truckNumber = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                val number = truckNumber?.let { st -> getTrackNumber(st) } ?: i

                val chapter = Chapter(it.toUri(), "Chapter $i", number, duration!!.toInt())
                i++;

                chapters.add(chapter)
            }
        }
        return chapters
    }

    private fun getBookDuration(chapters: List<Chapter>): Int {
        var duration = 0
        chapters.forEach {
            duration += it.duration
        }
        return duration
    }

    private fun getTrackNumber(string: String): Int {
        val trackNumber = string.split("/")[0]
        return trackNumber.toInt()
    }

    private fun getCover(chapter: Chapter): Bitmap? {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(chapter.uri.toString())
        val cover = metadataRetriever.embeddedPicture
        return cover?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    fun getBookFolderUri(bookName: String): Uri {
        val bookFolder = File(context.getExternalFilesDir(""), bookName)
        return bookFolder.toUri()
    }

    fun getBookByName(bookName: String): AudioBook {
        if (books.isEmpty()) {
            getBooks()
        }
        return books.find { it.title == bookName }!!
    }

    fun getBookByFolderName(bookFolderName: String): AudioBook {
        if (books.isEmpty()) {
            getBooks()
        }
        return books.find { it.chapterFolderName == bookFolderName }!!
    }

    companion object {
        private val INSTANCE: BookRepository? = null

        fun getInstance(context: Context): BookRepository {
            return INSTANCE ?: BookRepository(context)
        }
    }

}
