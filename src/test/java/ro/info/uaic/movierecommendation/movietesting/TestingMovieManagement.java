package ro.info.uaic.movierecommendation.movietesting;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.crossstore.ChangeSetPersister;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ro.info.uaic.movierecommendation.dtoresponses.movies.ActorDto;
import ro.info.uaic.movierecommendation.dtoresponses.movies.MovieDto;
import ro.info.uaic.movierecommendation.models.movies.Movie;
import ro.info.uaic.movierecommendation.models.movies.MovieType;
import ro.info.uaic.movierecommendation.services.movies.MovieService;

import java.util.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.info.uaic.movierecommendation.models.movies.Type.ACTION;
import static ro.info.uaic.movierecommendation.models.movies.Type.COMEDY;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser
class TestingMovieManagement {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Autowired
    private MovieService movieService;

    @MockBean
    @Autowired
    private ModelMapper mapper;

    @LocalServerPort
    private int PORT;

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

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setName("Curierul1");
        MovieDto postDto = mapper.map(movie, MovieDto.class);

        Mockito.when(movieService.getMovieById(movie.getId())).thenReturn(postDto);
        mockMvc.perform(get("http://localhost:" + PORT + "/api/v1/movies/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
                //.andExpect(jsonPath("$.name").value("Curierul1"));
    }

    @Test
    public void testGetAllMovies() throws Exception {

        Mockito.mock(MovieService.class);

        MovieDto MOVIE_1 = new MovieDto("Curierul1", Arrays.asList(new ActorDto("Jason Statham")),
                Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);
        MovieDto MOVIE_2 = new MovieDto("Curierul2", Arrays.asList(new ActorDto("Jason Statham")),
                Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);
        MovieDto MOVIE_3 = new MovieDto("Curierul3", Arrays.asList(new ActorDto("Jason Statham")),
                Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);

        List<MovieDto> movies = new ArrayList<>(Arrays.asList(MOVIE_1, MOVIE_2, MOVIE_3));

        final Pageable sourcePageable = mock(Pageable.class);


        Mockito.when(sourcePageable.getPageNumber()).thenReturn(0);
        Mockito.when(sourcePageable.getPageSize()).thenReturn(3);

        Mockito.when(movieService.findAll(sourcePageable)).thenReturn((Map<String, Object>) movies);
        mockMvc.perform(get("http://localhost:" + PORT + "/api/v1/movies/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
                //andExpect(jsonPath("$[1].name").value("Curierul2"));


    }

    @Test
    public void testCreateMovie() throws Exception {
        MovieDto mockMovie = new MovieDto("Curierul", Arrays.asList(new ActorDto("Jason Statham")),
                Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);
        String exampleMovieJson = "{\n" + "\"name\": " + "\"Curierul8\"," + "\n" + "" + "\"actors\": [\n" + "" + "{\n" + "" + "\"name\": \"Jason Statham\"\n" + "\n" + "" + " }\n" + "],\n" + "\"type\":[\n" + "" + "{\n" + "\"type\" : \"ACTION\"\n" + "}," + "\n" + "{\n" + " \"type\" : \"COMEDY\"\n" + "}\n" + "],\n" + "\"hasCaptions\": true,\n" + "}";


        Mockito.when(movieService.createMovie(Mockito.any(MovieDto.class))).thenReturn(mockMovie);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://localhost:" + PORT + "/api/v1/movies/create")
                .accept(MediaType.APPLICATION_JSON).content(exampleMovieJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertEquals("http://localhost:8080/api/v1/movies/create", response.getHeader(HttpHeaders.LOCATION));

    }

    @Test
    public void testDeleteMovieById() throws Exception {
        Mockito.when(movieService.getMovieById(5l)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ChangeSetPersister.NotFoundException))
                .andExpect(result -> assertEquals("Movie with ID 1 does not exist.", result.getResolvedException()
                        .getMessage()));
    }

    @Test
    public void testUpdateMovieById() throws Exception {
        MovieDto updatedMovie = new MovieDto("Curierul", Arrays.asList(new ActorDto("Jason Statham")),
                Arrays.asList(new MovieType(ACTION), new MovieType(COMEDY)), true);

        Movie movie = new Movie();
        movie.setId(1L);

        MovieDto postDto = mapper.map(movie, MovieDto.class);

        Mockito.when(movieService.getMovieById(movie.getId()))
                .thenReturn(postDto);
        Mockito.when(movieService.updateMovie(movie.getId(),postDto)).thenReturn(updatedMovie);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("http://localhost:8080/api/v1/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(updatedMovie));

        mockMvc.perform(mockRequest)
                .andExpect(status().isResetContent()).andDo(MockMvcResultHandlers.print());
                //.andExpect(jsonPath("$", notNullValue()))
                //.andExpect(jsonPath("$.name").value("Curierul"));
    }

}
