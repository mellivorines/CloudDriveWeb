package io.github.mellivorines.cloud.drive.web.service

import io.github.mellivorines.cloud.drive.web.dao.entity.UserFile
import io.github.mellivorines.cloud.drive.web.dao.entity.UserSearchHistory
import io.github.mellivorines.cloud.drive.web.dao.input.UserSearchHistoryInput
import io.github.mellivorines.cloud.drive.web.dao.repository.UserSearchHistoryRepository
import org.springframework.stereotype.Service


@Service
class UserSearchHistoryService(private val userSearchHistoryRepository: UserSearchHistoryRepository) {

    /**
     * 获取用户最新的搜索历史列表，默认十条
     *
     * @param userId
     * @return
     */
    fun list(userId: String): List<UserSearchHistory>? {
        return userSearchHistoryRepository.findByUserIdOrderByUpdateTime(userId)
    }

    /**
     * 保存用户搜索关键字，重复关键字会变为置顶状态
     *
     * @param searchContent
     * @param userId
     * @return
     */
    fun save(searchContent: String, userId: String): List<UserSearchHistory>? {
        saveSearchContent(searchContent, userId)
        return list(userId)
    }

    /**
     * 拼装实体信息
     *
     * @param searchContent
     * @param userId
     * @return
     */
    private fun assembleRPanUserSearchHistoryEntity(searchContent: String, userId: String): UserSearchHistory {
        val rPanUserSearchHistory = UserSearchHistoryInput(null, userId, searchContent, null, null)
        return rPanUserSearchHistory.toEntity()
    }

    /**
     * 保存搜索历史信息
     *
     * @param searchContent
     * @param userId
     */
    private fun saveSearchContent(searchContent: String, userId: String) {
        userSearchHistoryRepository.save(assembleRPanUserSearchHistoryEntity(searchContent, userId))
    }


}
