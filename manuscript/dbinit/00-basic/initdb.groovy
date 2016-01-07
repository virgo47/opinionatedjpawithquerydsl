import static DbInit.*

recreate('create.sql')

def collie = breed('collie')
def germanShepherd = breed('german shepherd')

dog('Lassie', collie)
dog('Rex', germanShepherd)

static Integer breed(String name) {
  def selector = [name: name]
  def id = findOrCreate("Breed", selector, selector) as Integer
  return id
}

static Integer dog(String name, Integer breed) {
  def selector = [name: name]
  def id = findOrCreate("Dog", selector, [breed_id: breed]) as Integer
  return id
}

