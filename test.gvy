import java.io.*;
import groovy.io.*;
import java.util.regex.*;
import Parser.AngularParser;
import Sorter.BuildSorter; 

def projectPath = "C:/Users/cem.topkaya/git/gui_nrf_test/"
def map = AngularParser.parseAngularJson(projectPath)
//println map
res = BuildSorter.getSortedLibraries(map)
println res
