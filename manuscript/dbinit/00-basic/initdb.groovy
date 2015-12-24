import static DbInit.*

recreate('create.sql')

dog('Lessie')
dog('Rex', 'german shepherd')

static Integer dog(String name, String breed = 'collie') {
	def selector = [name: name]
	def id = findOrCreate("Dog", selector, [breed: breed]) as Integer
	return id
}

