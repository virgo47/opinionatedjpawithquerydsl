import static DbInit.*

connect('jdbc:h2:tcp://localhost/~/sqldemo', 'sa', '')

dog('Lessie')
dog('Rex', 'german shepherd')

static Integer dog(String name, String breed = 'collie') {
	def selector = [name: name]
	def id = findOrCreate("Dog", selector, [breed: breed]) as Integer
	return id
}

