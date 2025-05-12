package com.readyChatAI.readyChatMessages.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readyChatAI.readyChatMessages.data.local.entities.CategoryEntity

@Dao
interface ReadyChatMessagesDao {

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM CategoryEntity WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCategory(category: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity")
    suspend fun getCategories(): List<CategoryEntity>
    /*

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertBusiness(business: BusinessEntity)

     @Transaction
     suspend fun loginBusiness(currentBusiness: BusinessEntity) {
         insertBusiness(currentBusiness)
     }

     @Transaction
     suspend fun logOut() {
         try {
             deleteAllBusiness()
             deleteAllEmployers()
         } catch (e: Exception) {
             Log.e(Constants.AUTH_TAG, "Error deleting the business and employers: ${e.message}")
         }
     }

     @Transaction
     suspend fun loginEmployer(currentEmployer: EmployerEntity) {
         insertEmployer(currentEmployer)
     }

     @Query("SELECT * FROM EmployerEntity")
     suspend fun getEmployer(): EmployerEntity?

     // Tables
     @Query("DELETE FROM TableEntity")
     suspend fun deleteAllTables()

     @Delete
     suspend fun deleteTables(tablesToRemove: List<TableEntity>)

     @Query("SELECT * FROM TableEntity WHERE userId = :businessId")
     suspend fun getTablesByBusinessId(businessId: String): List<TableEntity>

     @Query("SELECT * FROM TableEntity WHERE id = :tableId")
     suspend fun getTableById(tableId: String): TableEntity?

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertTables(tables: List<TableEntity>)

     @Query("SELECT * FROM EmployerEntity WHERE id = :employerId")
     suspend fun getEmployerById(employerId: String): EmployerEntity?

     /*@Query("SELECT * FROM TableEntity")
     suspend fun getAllTables(): List<TableEntity>*/

     // Contracts
     @Query("DELETE FROM ContractEntity")
     suspend fun deleteAllContracts()

     @Delete
     suspend fun deleteContracts(contractsToRemove: List<ContractEntity>)

     @Query("SELECT * FROM ContractEntity WHERE userId = :userId")
     suspend fun getContractsByUserId(userId: String): List<ContractEntity>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertContracts(contracts: List<ContractEntity>)

     // Resource
     @Query("DELETE FROM ResourceEntity")
     suspend fun deleteAllResources()

     @Delete
     suspend fun deleteAResource(resource: ResourceEntity)

     @Delete
     suspend fun deleteResources(resourcesToRemove: List<ResourceEntity>)

     @Query("SELECT * FROM ResourceEntity WHERE userId = :businessId")
     suspend fun getResourcesByBusinessId(businessId: String): List<ResourceEntity>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertResources(resources: List<ResourceEntity>)

     // Menu
     @Query("SELECT * FROM MenuEntity WHERE userId = :businessId")
     suspend fun getMenusByBusinessId(businessId: String): List<MenuEntity>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMenus(menus: List<MenuEntity>)

     @Delete
     suspend fun deleteMenus(menusToRemove: List<MenuEntity>)

     @Delete
     suspend fun deleteMenu(menu: MenuEntity)

     @Query("SELECT * FROM MenuResourceCrossRefEntity WHERE menuId = :menuId")
     suspend fun getResourceByMenuId(menuId: String): List<MenuResourceCrossRefEntity>

     @Query("SELECT * FROM ResourceEntity WHERE id = :resourceId")
     suspend fun getResourceById(resourceId: String): ResourceEntity

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMenuResourceCrossRef(menuResourceCrossRefEntity: List<MenuResourceCrossRefEntity>)

     @Query("DELETE FROM MenuResourceCrossRefEntity WHERE menuId IN (:menusResourceCrossRefToRemove)")
     suspend fun deleteMenusResourceCrossRef(menusResourceCrossRefToRemove: List<String>)

     @Query("DELETE FROM MenuResourceCrossRefEntity WHERE menuId = :menusId AND resourceId = :resourceId")
     suspend fun deleteMenusResourceCrossRefForResource(menusId: String, resourceId: String)

     @Query("SELECT * FROM MenuEntity WHERE id = :menuId")
     suspend fun getMenuById(menuId: String): MenuEntity?

     // News
     @Query("SELECT * FROM NewsEntity")
     suspend fun getNews(): List<NewsEntity>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertNews(news: List<NewsEntity>)

     @Delete
     suspend fun deleteNews(newsToRemove: List<NewsEntity>)

     @Query(
         """
             SELECT *
             FROM companylistingentity
             WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                 UPPER(:query) == symbol
         """
     )
     suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>*/
}