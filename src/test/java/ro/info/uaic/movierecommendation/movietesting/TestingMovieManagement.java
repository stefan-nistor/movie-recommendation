/*
package ro.info.uaic.movierecommendation.movietesting;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ro.info.uaic.movierecommendation.controllers.movies.MovieController;
import ro.info.uaic.movierecommendation.dtoresponses.movies.ActorDto;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ro.info.uaic.movierecommendation.models.movies.Type.ACTION;
import static ro.info.uaic.movierecommendation.models.movies.Type.COMEDY;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MovieController.class)
@WithMockUser
public class TestingMovieManagement {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ModelMapper mapper;

    @Test
    public void testConvertMovieEntityToMovieDto() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setName("Title");
        MovieDto postDto = mapper.map(movie, MovieDto.class);
        assertEquals(movie.getName(), postDto.getName());

    }

    @Test
    public void testConvertMovieDtoToMovieEntity() {
        MovieDto movieDto = new MovieDto();

        movieDto.setName("Title");

        Movie movie = mapper.map(movieDto, Movie.class);
        assertEquals(movie.getName(), movieDto.getName());

    }

    @Test
    public void testCreateMovie() throws Exception {
        MovieDto mockMovie = new MovieDto("Curierul",
                Arrays.asList(new ActorDto("Jason Statham")), Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);
        String exampleMovieJson = "{\n" +
                "    \"name\": \"Curierul8\",\n" +
                "        \"actors\": [\n" +
                "            {\n" +
                "            \"name\": \"Jason Statham\"\n" +
                "\n" +
                "            }\n" +
                "        ],\n" +
                "        \"type\":[\n" +
                "             {\n" +
                "                 \"type\" : \"ACTION\"\n" +
                "             },\n" +
                "             {\n" +
                "                 \"type\" : \"COMEDY\"\n" +
                "             }\n" +
                "        ],\n" +
                "        \"hasCaptions\": true,\n" +
                "}";


        Mockito.when(
                movieService.createMovie(Mockito.any(MovieDto.class))).thenReturn(mockMovie);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("api/v1/movies")
                .accept(MediaType.APPLICATION_JSON).content(exampleMovieJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertEquals("http://localhost/api/v1/movies",
                response.getHeader(HttpHeaders.LOCATION));

    }
}
*/
