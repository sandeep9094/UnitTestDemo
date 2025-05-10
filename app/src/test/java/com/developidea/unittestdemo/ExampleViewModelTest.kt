package com.developidea.unittestdemo

import app.cash.turbine.test
import com.developidea.unittestdemo.api.ApiResult
import com.developidea.unittestdemo.api.ApiService
import com.developidea.unittestdemo.data.model.ArticlesItem
import com.developidea.unittestdemo.data.model.NewsResponse
import com.developidea.unittestdemo.data.model.Source
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleViewModelTest {

    private val apiKey ="abcdef"
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ExampleViewModel
    private val mockApiService = mock<ApiService>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ExampleViewModel(mockApiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchNews 1 with successful response `() = runTest {
        val dummySource = Source(
            id = "the-washington-post",
            name = "The Washington Post"
        )

        val dummyArticle = ArticlesItem(
            source = dummySource,
            author = "Shayna Jacobs, Salvador Rizzo, Jeremy Roebuck, Perry Stein",
            title = "Justice Dept. investigating N.Y. attorney general who has targeted Trump - The Washington Post",
            description = "The probe of real estate records is the first known criminal investigation of a prosecutor who previously took action against President Donald Trump.",
            url = "https://www.washingtonpost.com/national-security/2025/05/08/letitia-james-mortgage-investigation-trump-justice-fbi/",
            urlToImage = "https://www.washingtonpost.com/wp-apps/imrs.php?src=https://arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/S7JBR63PRJVBYJBNPUM2MDZKQE_size-normalized.jpg&w=1440",
            publishedAt = "2025-05-09T06:17:46Z",
            content = "NEW YORK The Justice Department has opened a criminal investigation into real estate transactions involving New York Attorney General Letitia James, according to a person familiar with the case the f… [+7033 chars]"
        )

        val expectedResponse = NewsResponse(
            status = "ok",
            totalResults = 34,
            articles = listOf(dummyArticle)
        )

        //Mock Response
        whenever(mockApiService.getNews(apiKey)).thenReturn(Response.success(expectedResponse))

        //Act
        viewModel.newsResult.filterNotNull().test {
            viewModel.getNews(apiKey)
            //awaitItem() for flow to get to next state, if current state is Loading
            assertEquals(ApiResult.Loading, awaitItem())

            //awaitItem() for flow to get to next state, if current state is Success
            val result = awaitItem()
            assertTrue(result is ApiResult.Success)

            //After reaching success state, we will verify its result
            val successResult = viewModel.newsResult.value
            assertEquals(expectedResponse, (successResult as ApiResult.Success).data)
        }
    }

}
