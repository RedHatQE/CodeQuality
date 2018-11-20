import unittest
from a import world

class TestA(unittest.TestCase):

	def test_upper(self):
		self.assertEqual(world(), "Hello World, It's me!")

unittest.main()
