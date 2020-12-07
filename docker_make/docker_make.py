import sys
import os
import docker
ABSTRACT_ROOT_TAG = '/'
dck = docker.from_env()
class TagNode:
	def __init__(self, tag, parent = None):
		self.tag = tag
		self.childs = []
		self.parent = parent
	def addChild(self,node):
		self.childs.append(node)
	def __str__(self, level = 0):
		res= "|"+level*('--')+'> '+self.tag+'\n'
		for c in self.childs:
			res+=c.__str__(level=level+1)
		return res
	def getTag(self):
		return self.tag
class Tree:
	def __init__(self,root, direct):
		self.root = root
		self.direct = direct
class ImageInfo:
	def __init__(self):
		pass
	
def read_dockermakefile(makefile):
	current = None
	res = {}
	with open(makefile,'r') as file:
		lines = [line for line in file.read().split('\n') if line != '']
		for line_nb, line in enumerate(lines):
			if line == "image>>":
				if current is not None:
					res[current.tag]=current
				current = ImageInfo()
			else:
				key, val = line.split("=")
				if key=="tag":
					current.tag = val
				elif key == "dir":
					current.dir = val
	if current is not None:
		res[current.tag]=current
	return res
def read_tagtree(treefile, indent_char = '\t'):
	res = TagNode(ABSTRACT_ROOT_TAG)
	level = -1
	current = res
	direct = {}
	with open(treefile,'r') as file:
		text = ([line for line in file.read().split('\n') if line != ''])
		for line_nb, line in enumerate(text):
			l = 0
			tag=""
			for c in line:
				if c == indent_char:
					l+=1
				else:
					tag+=c
			print("[TREE-PARSE][INFO]","Reading tag :",tag,"at level :",l)
			node = TagNode(tag)
			direct[tag] = node
			if l < level:
				node.parent = current.parent
				for i in range(level-l):
					node.parent = node.parent.parent
			elif l == level:
				node.parent = current.parent
			elif l == level+1:
				node.parent = current
			else:
				print("[TREE-PARSE][ERROR]","Too much indentation on tag :",tag)
				exit(1)
			print("[TREE-PARSE][INFO]","Linking to parent :"+node.parent.tag)
			node.parent.addChild(node)
			current = node
			level = l
	return Tree(res, direct)

def dfs(root):
	q = [root]
	res = []
	check = set()
	while(len(q)>0):
		node = q[0]
		leaf = True
		for c in node.childs:
			if not(c.getTag() in check):
				leaf = False
				check.add(c.getTag())
				q.insert(0,c)
		if leaf:
			res.append(node.getTag())
			del q[0]
	return res

def bfs(root):
	res= []

	q = [root]
	while len(q) > 0:
		node = q[0]
		del q[0]
		res.append(node.getTag())
		for c in node.childs:
			q.append(c)
	return res

def remove_image(tag, dck=dck):
	dck.images.remove(image=tag)

def build_image(tag,infos, dck = dck):
	print("Building image :",tag)
	img=dck.images.build(path=infos[tag].dir, rm=True)
	t1, t2 = tag.split(':')
	img.tag(t1,tag=t2)

def traversal(tree, order, action, **kwargs):
	ordering = order(tree)
	for tag in ordering:
		if tag!=ABSTRACT_ROOT_TAG:
			action(tag, **kwargs)

def read_image_info(tag, dck=dck):
	print("Reading image :",tag)
	img = dck.images.get(tag)
	print(img.attrs)

class Params:
	def __init__(self,args):
		self.treefile = "tree"
		self.makefile = "dockermakefile"
		self.workdir = "."
		self.operation = "help"
		self.target = None
		parse={}
		for a in args:
			opt,val = a.split('=')
			parse[opt] = val
		if("tf" in parse):
			self.treefile = parse["tf"]
		if("mf" in parse):
			self.makefile = parse["mf"]
		if("wd" in parse):
			self.workdir = parse["wd"]
		if("op" in parse):
			self.operation = parse["op"]
		if("tgt" in parse):
			self.target = parse["tgt"]
		
def main():
	args = sys.argv[1:]
	print("Launched with arguments :",args)
	params = Params(args)
	os.chdir(params.workdir)
	tree = read_tagtree(params.treefile)
	infos = read_dockermakefile(params.makefile)
	root = tree.direct[params.target] if params.target is not None else tree.root
	if params.operation == "build":
		traversal(root, bfs, build_image, infos=infos)
	
if __name__ == '__main__':
	main()
