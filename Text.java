public class Text
{
  double height = 1.0;
  double size = .01;

  double x, y;
  int layer = 0;
  int group = -1;
  boolean selected = false;
  boolean flipped = false;
  int id = 0;

  String text;

  Text(int temp_layer, String temp_text, double temp_height, double temp_size,
       double temp_x, double temp_y)
  {
    x = temp_x;
    y = temp_y;
    layer = temp_layer;
    text = temp_text;
    height = temp_height;
    size = temp_size;
  }
}

