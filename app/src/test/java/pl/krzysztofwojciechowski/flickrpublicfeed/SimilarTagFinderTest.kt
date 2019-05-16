package pl.krzysztofwojciechowski.flickrpublicfeed

import org.junit.Test

import org.junit.Assert.*

class SimilarTagFinderTest {
    private fun makeEmptyEntry(tags: String): FeedEntry {
        return FeedEntry("", "", "1970-01-01", tags)
    }
    @Test
    fun testSimilarFinderOperation() {
        val entry1 = makeEmptyEntry("foo, bar")
        val entry2 = makeEmptyEntry("foo, baz")
        val entry3 = makeEmptyEntry("baz")
        val entry4 = makeEmptyEntry("quux")

        val feed = FeedAdapter({ _: FeedEntry, _: List<FeedEntry> -> Unit }, mutableListOf(entry1, entry2, entry3, entry4))

        val similar1 = feed.findImagesWithMatchingTags(entry1)
        assertEquals(similar1, listOf(entry2))
        val similar2 = feed.findImagesWithMatchingTags(entry2)
        assertTrue(similar2.size == 2)
        assertTrue(similar2.contains(entry1))
        assertTrue(similar2.contains(entry3))
        val similar3 = feed.findImagesWithMatchingTags(entry3)
        assertEquals(similar3, listOf(entry2))
        val similar4 = feed.findImagesWithMatchingTags(entry4)
        assertTrue(similar4.isEmpty())
    }

    @Test
    fun testSimilarFinderLimiting() {
        val entry1 = makeEmptyEntry("foo, bar, baz")
        val entry2 = makeEmptyEntry("foo")
        val entries = mutableListOf(entry1, entry2)
        for (x in 0..SIMILAR_IMAGE_COUNT) {
            entries.add(makeEmptyEntry("foo"))
        }

        val feed = FeedAdapter({ _: FeedEntry, _: List<FeedEntry> -> Unit }, entries)

        val similar1 = feed.findImagesWithMatchingTags(entry1)
        // Make sure SIMILAR_IMAGE_COUNT is respected.
        assertEquals(similar1.size, SIMILAR_IMAGE_COUNT)
    }

    @Test
    fun testSimilarFinderOrdering() {
        val entry1 = makeEmptyEntry("foo, bar, baz")
        val entry2 = makeEmptyEntry("foo, bar")
        val entries = mutableListOf(entry1, entry2)
        for (x in 0..SIMILAR_IMAGE_COUNT) {
            entries.add(makeEmptyEntry("foo"))
        }

        val feed = FeedAdapter({ _: FeedEntry, _: List<FeedEntry> -> Unit }, entries)

        val similar1 = feed.findImagesWithMatchingTags(entry1)
        // Make sure entries with more tags in common are shown first.
        assertEquals(similar1[0], entry2)
    }
}
