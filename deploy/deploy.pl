# parameters: all paths should be absolute
# # recommended max_mem: 16g
# # recommended start_mem: 8g
#
# $code_path = $1
# $data_path = $2
# $log_path = $3
# $max_mem = $4
# $start_mem = $5
#
# #one time
# mkdir -p $1
# svn co http://agni.cleartrip.com/svn/repos/ct_smallworld/search/trunk $1/search

use Getopt::Long;
use strict;

# defaults
my $component = "search";
my $bootstrap = 0;
my $environment = "adichad-ext";
my $ver_type = "trunk";
my $ver = "nononono";
my $code_path = $ENV{HOME}."/smallworld";
my $data_path = $code_path."/data";
my $log_path = $code_path."/logs";
my $jdk_arch = "linux-x64"; #linux-i586
my $jdk_ver = "7u4";
my $jdk_ver_deployed = "1.7.0_04";
my $tomcat_ver = "7.0.25";
my $mongo_arch = "linux-x86_64";
my $mongo_ver = "2.0.5";
my $with_mongo = 0;
my $index_dir = "index";
my $restart = 0;
my $with_jdk = 0;
my $with_tomcat = 0;

#sysctl -w fs.file-max=100000
#cat /proc/sys/fs/file-max
#vi /etc/sysctl.conf
#fs.file-max=100000
#http://www.cyberciti.biz/faq/linux-increase-the-maximum-number-of-open-files/


my $repo_url = "http://agni.cleartrip.com/svn/repos/ct_smallworld";
my $cmdres = GetOptions (
  "environment=s" => \$environment,
  "ver=s" => \$ver,
  "ver-type=s" => \$ver_type,
  "code-path=s" => \$code_path,
  "data-path=s" => \$data_path,
  "log-path=s" => \$log_path,
  "jdk-arch=s" => \$jdk_arch,
  "jdk-ver=s" => \$jdk_ver,
  "tomcat-ver=s" => \$tomcat_ver,
  "with-mongo" => \$with_mongo,
  "mongo-arch" => \$mongo_arch,
  "mongo-ver" => \$mongo_ver,
  "bootstrap" => \$bootstrap,
  "with-jdk" => \$with_jdk,
  "with-tomcat" => \$with_tomcat,
  "restart" => \$restart
);

if ($ver_type eq "branch") {
  $ver_type = "branches";
} elsif ($ver_type eq "tag") {
  $ver_type = "tags";
}

my $index_path = "$data_path/$index_dir";

print "_______ smallworld $component installation _______\n";
print "         bootstrap : yes\n" unless !$bootstrap;
print "       environment : $environment\n";
print "  trunk/branch/tag : $ver_type\n";
print "           version : $ver\n" unless $ver_type eq "trunk";
print "         code path : $code_path\n";
print "         data path : $data_path\n";
print "        index path : $index_path\n";
print "          log path : $log_path\n";
print "  jvm architecture : $jdk_arch\n" unless (!$with_jdk);
print "       jvm version : $jdk_ver\n" unless (!$with_jdk);
print "    tomcat version : $tomcat_ver\n" unless (!$with_tomcat);
print "mongo architecture : $mongo_arch\n" unless (!$with_mongo);
print "     mongo version : $mongo_ver\n" unless (!$with_mongo);
print "    restart tomcat : yes\n" unless (!$restart);
print "______________________________________________\n";

if($with_mongo) {
  if(system("rm -f $code_path/mongodb*")) {
    print "[ WARN] unable to remove old mongo\n";
  }

  if(system("svn export $repo_url/third-party/installables/mongo/mongodb-$mongo_arch-$mongo_ver.tgz $code_path/mongodb-$mongo_arch-$mongo_ver.tgz")) {
    die "[FATAL] unable to fetch mongodb-$mongo_arch-$mongo_ver.tgz to $code_path\n";
  }

  if(system("cd $code_path && tar -xzf $code_path/mongodb-$mongo_arch-$mongo_ver.tgz")) {
    die "[FATAL] unable to untar mongo\n";
  }

  if(system("rm -f $code_path/mongodb-$mongo_arch-$mongo_ver.tgz")) {
    print "[ WARN] unable to delete mongo archive\n";
  }

  if(system("unlink $code_path/mongodb")) {
    print "[ WARN] unable to unlink mongo soft symlink at $code_path/mongodb\n";
  }

  if(system("ln -s $code_path/mongodb-$mongo_arch-$mongo_ver $code_path/mongodb")) {
    die "[FATAL] unable to create mongo soft symlink at $code_path/mongodb\n";
  }
}

if($with_tomcat) {
  if(system("rm -f $code_path/apache-tomcat-$tomcat_ver.tar.gz")) {
    print "[ WARN] unable to delete tomcat archive\n";
  }

  if(system("svn export $repo_url/third-party/installables/tomcat/apache-tomcat-$tomcat_ver.tar.gz $code_path/apache-tomcat-$tomcat_ver.tar.gz")) {
    die "[FATAL] unable to fetch apache-tomcat-$tomcat_ver.tar.gz to $code_path\n";
  }

  if(system("rm -rf $code_path/apache-tomcat-$tomcat_ver")) {
    print "[ WARN] unable to remove old tomcat\n";
  }

  if(system("cd $code_path && tar -xzf $code_path/apache-tomcat-$tomcat_ver.tar.gz")) {
    die "[FATAL] unable to untar tomcat\n";
  }

  if(system("unlink $code_path/apache-tomcat")) {
    print "[ WARN] unable to unlink apache tomcat soft symlink at $code_path/apache-tomcat\n";
  }

  if(system("ln -s $code_path/apache-tomcat-$tomcat_ver $code_path/apache-tomcat")) {
    die "[FATAL] unable to create apache tomcat soft symlink at $code_path/apache-tomcat\n";
  }

  if(system("rm -f $code_path/apache-tomcat-$tomcat_ver.tar.gz")) {
    print "[ WARN] unable to delete tomcat archive\n";
  }
}

if($with_jdk) {
  if(system("rm -f $code_path/jdk-$jdk_ver-$jdk_arch.tar.gz")) {
    print "[ WARN] unable to delete jdk archive\n";
  }

  if(system("svn export $repo_url/third-party/installables/jdk/jdk-$jdk_ver-$jdk_arch.tar.gz $code_path/jdk-$jdk_ver-$jdk_arch.tar.gz")) {
    die "[FATAL] unable to fetch jdk-$jdk_ver-$jdk_arch.tar.gz to $code_path\n";
  }

  if(system("rm -rf $code_path/jdk$jdk_ver_deployed")) {
    print "[ WARN] unable to remove old jdk\n";
  }

  if(system("cd $code_path && tar -xzf $code_path/jdk-$jdk_ver-$jdk_arch.tar.gz")) {
    die "[FATAL] unable to untar jdk\n";
  }

  if(system("unlink $code_path/jdk")) {
    print "[ WARN] unable to unlink jdk soft symlink at $code_path/jdk\n";
  }

  if(system("ln -s $code_path/jdk$jdk_ver_deployed $code_path/jdk")) {
    die "[FATAL] unable to create jdk soft symlink at $code_path/jdk\n";
  }

  if(system("rm -f $code_path/jdk-$jdk_ver-$jdk_arch.tar.gz")) {
    print "[ WARN] unable to delete jdk archive\n";
  }
}
if($bootstrap) {
  if(system("mkdir -p $code_path")) {
    die "[FATAL] unable to create directory at: $code_path\n";
  }

  if(system("mkdir -p $index_path")) {
    die "[FATAL] unable to create directory at: $index_path\n";
  }

  if(system("mkdir -p $log_path/archive")) {
    die "[FATAL] unable to create directory at: $log_path\n";
  }

  if($restart) {
    if(system("$code_path/bin/stop-$component.sh")) {
      print "[ WARN] unable to stop smallworld-$component using $code_path/bin/stop-$component.sh\n";
    } else {
      sleep(1);
    }
  }


  if(system("mkdir -p $code_path/bin")) {
    die "[FATAL] unable to create bin folder\n";
  }

  if(system("rm -rf $code_path/$component")) {
    die "[FATAL] unable to remove directory at: $code_path/$component\n";
  }

  if(system("svn co --ignore-externals -q $repo_url/$component $code_path/$component")) {
    die "[FATAL] unable to checkout working copy of SmallWorld $component to $code_path/$component\n";
  }

  if(system("unlink $code_path/bin/start-$component.sh")) {
    print "[ WARN] unable to remove old startup script\n";
  }

  if(system("ln -s $code_path/$component/env/$environment/start.sh $code_path/bin/start-$component.sh")) {
    die "[FATAL] unable to create start script symlimk at $code_path/bin/start-$component.sh\n";
  }

  if(system("unlink $code_path/$component/current")) {
    print "[ WARN] unable to unlink old deployed directory script\n";
  }

  if(system("ln -s $code_path/$component/$ver_type".(($ver_type eq "trunk")?"":"/$ver")." $code_path/$component/current")) {
    die "[FATAL] unable to link to specified version $ver/$ver_type\n";
  }

  if(system("ln -s $code_path/$component/current/build/smallworld-$component.war $code_path/$component/env/$environment/catalina_base/webapps/smallworld-$component.war")) {
    die "[FATAL] unable to create symlink to smallworld-$component.war at $code_path/$component/env/$environment/catalina_base/webapps/smallworld-$component.war\n";
  }

  if($restart) {
    if(system("$code_path/bin/start-$component.sh")) {
      die "[FATAL] unable to start smallworld-$component using $code_path/bin/stop-$component.sh\n";
    }
  }

  if(system("unlink $code_path/bin/stop-$component.sh")) {
    print "[ WARN] unable to remove old stop script\n";
  }

  if(system("ln -s $code_path/$component/env/$environment/stop.sh $code_path/bin/stop-$component.sh")) {
    die "[FATAL] unable to create stop script symlimk at $code_path/bin/stop-$component.sh\n";
  }

}
else {
  if($restart) {
    if(system("$code_path/bin/stop-$component.sh")) {
      print "[ WARN] unable to stop smallworld-$component using $code_path/bin/stop-$component.sh\n";
    }

    sleep(3);
  }
  if(system("rm -rf $code_path/$component/env/$environment/catalina_base/webapps/smallworld-$component")) {
    die "[FATAL] unable to remove old unpacked war folder smallworld-$component\n";
  }

  if(system("svn up --ignore-externals $code_path/$component/env/$environment")) {
    die "[FATAL] unable to update smallworld-$component env on $environment at $code_path/$component/env/$environment\n";
  }

  if(system("svn up --ignore-externals $code_path/$component/$ver_type".(($ver_type eq "trunk")?"":"/$ver"))) {
    die "[FATAL] unable to update smallworld-$component at $code_path/$component/$ver_type".(($ver_type eq "trunk")?"":"/$ver")."\n";
  }

  if(system("unlink $code_path/$component/current")) {
    print "[ WARN] unable to unlink old deployed directory script\n";
  }

  if(system("ln -s $code_path/$component/$ver_type".(($ver_type eq "trunk")?"":"/$ver")." $code_path/$component/current")) {
    die "[FATAL] unable to link to specified version $ver/$ver_type\n";
  }

  if(system("unlink $code_path/$component/env/$environment/catalina_base/webapps/smallworld-$component.war")) {
    print "[ WARN] unable to unlink old war\n";
  }

  if(system("ln -s $code_path/$component/current/build/smallworld-$component.war $code_path/$component/env/$environment/catalina_base/webapps/smallworld-$component.war")) {
    die "[FATAL] unable to create symlink to smallworld-$component.war at $code_path/$component/env/$environment/catalina_base/webapps/smallworld-$component.war\n";
  }
  
  if($restart) {
    if(system("$code_path/bin/start-$component.sh")) {
      die "[FATAL] unable to start smallworld-$component using $code_path/bin/start-$component.sh\n";
    }
  }

}

print "_________________________ deployment complete _____________________\n";
print "to start/stop smallworld $component:-\n";
print "$code_path/bin/start-$component.sh\n";
print "$code_path/bin/stop-$component.sh\n";
print "to start mongodb:-\n";
print "$code_path/mongodb/bin/mongod\n";
print "___________________________________________________________________\n";

