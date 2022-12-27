public class Position {

    // vectores de posicion
    public static final Position DOWN = new Position(0, 1);
    public static final Position UP = new Position(0, -1);
    public static final Position LEFT = new Position(-1, 0);
    public static final Position RIGHT = new Position(1, 0);
    public static final Position ZERO = new Position(0, 0);
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // constructor para crear nueva posocion de los valores en otro
    public Position(Position positionToCopy) {
        this.x = positionToCopy.x;
        this.y = positionToCopy.y;
    }

    // setea la posicion especificada en X y Y
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Position otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }

    // calcula la distancia
    public double distanceTo(Position otherPosition) {
        return Math.sqrt(Math.pow(x - otherPosition.x, 2) + Math.pow(y - otherPosition.y, 2));
    }

    // multiplica ambos eementos de la posicion por un monto
    public void multiply(int monto) {
        x *= monto;
        y *= monto;
    }

    // Actualiza esta posición restando los valores de la otra posición
    public void subtract(Position otherPosition) {
        this.x -= otherPosition.x;
        this.y -= otherPosition.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
