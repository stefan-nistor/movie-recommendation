package ro.info.uaic.movierecommendation.movietesting;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ro.info.uaic.movierecommendation.controllers.movies.MovieController;
import ro.info.uaic.movierecommendation.dtoresponses.movies.ActorDto;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
    public void testGetMovieById() throws Exception {
        MovieDto movieDto = new MovieDto("Curierul1",
                Arrays.asList(new ActorDto("Jason Statham")), Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);
        Movie movie = mapper.map(movieDto, Movie.class);
        Mockito.when(movieService.getMovieById(movie.getId())).thenReturn(Optional.of(movie));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$.name", is("Curierul1")));
    }

    @Test
    public void testGetAllMovies() throws Exception {

        MovieDto MOVIE_1 = new MovieDto("Curierul1",
                Arrays.asList(new ActorDto("Jason Statham")), Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);
        MovieDto MOVIE_2 = new MovieDto("Curierul2",
                Arrays.asList(new ActorDto("Jason Statham")), Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);
        MovieDto MOVIE_3 = new MovieDto("Curierul3",
                Arrays.asList(new ActorDto("Jason Statham")), Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);

        List<MovieDto> movies = new ArrayList<>(Arrays.asList(MOVIE_1, MOVIE_2, MOVIE_3));
        final Pageable sourcePageable = mock(Pageable.class);
        Mockito.when(sourcePageable.getPageNumber()).thenReturn(1);
        Mockito.when(sourcePageable.getPageSize()).thenReturn(5);
        Mockito.when(movieService.findAll(sourcePageable)).thenReturn(movies);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(3)))
                .andExpect((ResultMatcher) jsonPath("$[2].name", is("Curierul2")));
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
