package com.example.dawid.snake.engine;

import android.provider.ContactsContract;

import com.example.dawid.snake.enums.Direction;
import com.example.dawid.snake.enums.GameState;
import com.example.dawid.snake.enums.TileType;
import com.example.dawid.snake.location.Coordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dawid on 15.05.2017.
 */

public class GameEngine
{
    public static final int GAME_WIDTH = 28;
    public static final int GAME_HEIGTH = 42;
    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snake = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();
    private List<Coordinate> worms = new ArrayList<>();
    private Direction currentDirection = Direction.EAST;
    private GameState currentGameState = GameState.Running;
    private Random random = new Random();
    private boolean increaseTail = false;
    private int numberOfApplesCollected;
    private boolean change;
    private long speed=200;
    private Coordinate getSnakeHead()
    {
        return snake.get(0);
    }

    public GameEngine()
    {
        numberOfApplesCollected = 0;
    }

    public int getNumberOfApplesCollected()
    {
        return numberOfApplesCollected;
    }

    public void setChange(boolean b)
    {
        this.change = b;
    }

    public void setSpeed(long i)
    {
        this.speed = i;
    }

    public void initGame()
    {
        addWalls();
        addSnake();
        addApples();
        if(speed <= 100)
        {
            addWorms();
        }
    }

    private void addApples() {
        Coordinate coordinate = null;
        boolean added = false;
        while(!added)
        {
            int x = 1 + random.nextInt( GAME_WIDTH - 2);
            int y = 1 + random.nextInt( GAME_HEIGTH - 2);

            coordinate = new Coordinate(x,y);
            boolean collision = false;
            for(Coordinate cor: snake)
            {
                if(cor.equals(coordinate))
                {
                    collision = true;
                }
            }

            for(Coordinate cor: apples)
            {
                if(cor.equals(coordinate))
                {
                    collision = true;
                }
            }

            for(Coordinate cor: worms)
            {
                if(cor.equals(coordinate))
                {
                    collision = true;
                }
            }
            added = !collision;
        }
        apples.add(coordinate);
    }

    private void addWorms()
    {
        Coordinate coordinate1 = null;
        Coordinate coordinate2 = null;
        Coordinate coordinate3 = null;
        boolean added = false;
        while(!added)
        {
            coordinate1 = new Coordinate(1 + random.nextInt( GAME_WIDTH - 2),1 + random.nextInt( GAME_HEIGTH - 2));
            coordinate2 = new Coordinate(1 + random.nextInt( GAME_WIDTH - 2),1 + random.nextInt( GAME_HEIGTH - 2));
            coordinate3 = new Coordinate(1 + random.nextInt( GAME_WIDTH - 2),1 + random.nextInt( GAME_HEIGTH - 2));
            boolean collision = false;
            for(Coordinate cor: snake)
            {
                if(cor.equals(coordinate1) || cor.equals(coordinate2) || cor.equals(coordinate3))
                {
                    collision = true;
                }
            }

            for(Coordinate cor: apples)
            {
                if(cor.equals(coordinate1) || cor.equals(coordinate2) || cor.equals(coordinate3))
                {
                    collision = true;
                }
            }
            added = !collision;
        }
        worms.add(coordinate1);
        worms.add(coordinate2);
        worms.add(coordinate3);
    }

    public void update()
    {
        //aktualizacja weza
        switch(currentDirection)
        {
            case NORTH:
                updateSnake(0, -1);
                break;
            case EAST:
                updateSnake(1, 0);
                break;
            case SOUTH:
                updateSnake(0, 1);
                break;
            case WEST:
                updateSnake(-1, 0);
                break;
        }
        if(change && speed <= 100)
        {
            worms.clear();
            addWorms();
            change = false;
        }
        //sprawdzenie uderzenia w sciane
        for (Coordinate w: walls)
        {
            if(snake.get(0).equals(w))
            {
                currentGameState = GameState.Lost;
            }
        }

        //sprawdzenie uderzenia w ogon
        for(int i=1; i< snake.size(); ++i)
        {
            if(getSnakeHead().equals(snake.get(i)))
            {
                currentGameState = GameState.Lost;
                return;
            }
        }

        //sprawdzenie jablka
        Coordinate applesToRemove = null;
        for(Coordinate cor: apples)
        {
            if(getSnakeHead().equals(cor))
            {
                applesToRemove = cor;
                increaseTail = true;
            }
        }
        if(applesToRemove != null)
        {
            apples.remove(applesToRemove);
            addApples();
        }

        //sprawdzenie robaka
        for(Coordinate cor: worms)
        {
            if(getSnakeHead().equals(cor))
            {
                currentGameState = GameState.Lost;
            }
        }
    }


    public TileType[][] getMap()
    {
        TileType[][] map = new TileType[GAME_WIDTH][GAME_HEIGTH];

        for(int i=0 ; i< GAME_WIDTH; ++i)
        {
            for(int j=0; j < GAME_HEIGTH; ++j)
            {
                map[i][j] = TileType.Nothing;
            }
        }

        for(Coordinate cor: snake)
        {
            map[cor.getX()][cor.getY()] = TileType.SnakeTail;
        }

        map[snake.get(0).getX()][snake.get(0).getY()] = TileType.SnakeHead;

        for(Coordinate wall: walls)
        {
            map[wall.getX()][wall.getY()] = TileType.Wall;
        }

        for(Coordinate apple: apples)
        {
            map[apple.getX()][apple.getY()] = TileType.Apple;
        }

        for(Coordinate worm: worms)
        {
            map[worm.getX()][worm.getY()] = TileType.Worms;
        }
        return map;
    }


    private void addWalls()
    {
        //gorna i dolna sciana
        for (int i =0 ; i < GAME_WIDTH; ++i)
        {
            walls.add(new Coordinate(i,0));
            walls.add(new Coordinate(i,GAME_HEIGTH-1));
        }

        // lewa i prawa sciana
        for(int i =1; i < GAME_HEIGTH; ++i)
        {
            walls.add(new Coordinate(0,i));
            walls.add(new Coordinate(GAME_WIDTH-1,i));
        }
    }

    private void addSnake()
    {
        snake.clear();
        snake.add(new Coordinate(7,7));
        snake.add(new Coordinate(6,7));
        snake.add(new Coordinate(5,7));
        snake.add(new Coordinate(4,7));
        snake.add(new Coordinate(3,7));
        snake.add(new Coordinate(2,7));
    }

    private void updateSnake(int x, int y)
    {
        int newX = snake.get(snake.size()-1).getX();
        int newY = snake.get(snake.size()-1).getY();

        for(int i= snake.size()-1; i > 0; i--)
        {
            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());
        }

        if(increaseTail)
        {
            snake.add(new Coordinate(newX, newY));
            numberOfApplesCollected++;
            increaseTail = false;
        }
        snake.get(0).setX(snake.get(0).getX() + x);
        snake.get(0).setY(snake.get(0).getY() + y);
    }

    public void setCurrentGameState(GameState state)
    {
        currentGameState = state;
    }

    public GameState getCurrentGameState()
    {
        return currentGameState;
    }

    public void updateDirection(Direction newDirection)
    {
        if( Math.abs(newDirection.ordinal() -currentDirection.ordinal()) % 2 == 1)
        {
            currentDirection = newDirection;
        }
    }
}
