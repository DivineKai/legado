package io.legado.app.ui.book.read.page.entities

import kotlin.math.min

@Suppress("unused")
data class TextChapter(
    val position: Int,
    val title: String,
    val url: String,
    val pages: List<TextPage>,
    val chaptersSize: Int,
    val isVip: Boolean,
    val isPay: Boolean,
) {

    fun getPage(index: Int): TextPage? {
        return pages.getOrNull(index)
    }

    fun getPageByReadPos(readPos: Int): TextPage? {
        return getPage(getPageIndexByCharIndex(readPos))
    }

    val lastPage: TextPage? get() = pages.lastOrNull()

    val lastIndex: Int get() = pages.lastIndex

    val lastReadLength: Int get() = getReadLength(lastIndex)

    val pageSize: Int get() = pages.size

    /**
     * @param index 页数
     * @return 是否是最后一页
     */
    fun isLastIndex(index: Int): Boolean {
        return index >= pages.size - 1
    }

    /**
     * @param pageIndex 页数
     * @return 已读长度
     */
    fun getReadLength(pageIndex: Int): Int {
        var length = 0
        val maxIndex = min(pageIndex, pages.size)
        for (index in 0 until maxIndex) {
            length += pages[index].charSize
        }
        return length
    }

    /**
     * @param length 当前页面文字在章节中的位置
     * @return 下一页位置,如果没有下一页返回-1
     */
    fun getNextPageLength(length: Int): Int {
        val pageIndex = getPageIndexByCharIndex(length)
        if (pageIndex + 1 >= pageSize) {
            return -1
        }
        return getReadLength(pageIndex + 1)
    }

    /**
     * @return 获取未读文字
     */
    fun getUnRead(pageIndex: Int): String {
        val stringBuilder = StringBuilder()
        if (pages.isNotEmpty()) {
            for (index in pageIndex..pages.lastIndex) {
                stringBuilder.append(pages[index].text)
            }
        }
        return stringBuilder.toString()
    }

    /**
     * 获取内容
     */
    fun getContent(): String {
        val stringBuilder = StringBuilder()
        pages.forEach {
            stringBuilder.append(it.text)
        }
        return stringBuilder.toString()
    }

    /**
     * @return 需要朗读的文本列表
     * @param pageIndex 起始页
     * @param pageSplit 是否分页
     * @param startPos 从当前页什么地方开始朗读
     */
    fun getNeedReadAloud(pageIndex: Int, pageSplit: Boolean, startPos: Int): String {
        //todo 未完成
        val stringBuilder = StringBuilder()
        if (pages.isNotEmpty()) {
            for (index in pageIndex..pages.lastIndex) {
                stringBuilder.append(pages[index].text)
                if (pageSplit && !stringBuilder.endsWith("\n")) {
                    stringBuilder.append("\n")
                }
            }
        }
        return stringBuilder.toString()
    }

    /**
     * @return 根据索引位置获取所在页
     */
    fun getPageIndexByCharIndex(charIndex: Int): Int {
        var length = 0
        pages.forEach {
            length += it.charSize
            if (length > charIndex) {
                return it.index
            }
        }
        return pages.lastIndex
    }
}