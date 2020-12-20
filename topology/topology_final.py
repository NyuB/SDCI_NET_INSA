# Copyright (c) 2015 SONATA-NFV and Paderborn University
# ALL RIGHTS RESERVED.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Neither the name of the SONATA-NFV, Paderborn University
# nor the names of its contributors may be used to endorse or promote
# products derived from this software without specific prior written
# permission.
#
# This work has been performed in the framework of the SONATA project,
# funded by the European Commission under Grant number 671517 through
# the Horizon 2020 and 5G-PPP programmes. The authors would like to
# acknowledge the contributions of their colleagues of the SONATA
# partner consortium (www.sonata-nfv.eu).
import logging
import sys
import time
from mininet.log import setLogLevel
from emuvim.dcemulator.net import DCNetwork
from emuvim.api.rest.rest_api_endpoint import RestApiEndpoint
from emuvim.api.openstack.openstack_api_endpoint import OpenstackApiEndpoint

logging.basicConfig(level=logging.INFO)
setLogLevel('info')  # set Mininet loglevel
logging.getLogger('werkzeug').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.base').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.compute').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.keystone').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.nova').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.neutron').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.heat').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.heat.parser').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.glance').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.helper').setLevel(logging.DEBUG)


def createHost(httpmode, net, name, image):
    if httpmode:
        return net.addDocker(name, dimage = image)
    else:
        return net.addHost(name)

def create_topology(httpmode=False):
    net = DCNetwork(monitor=False, enable_learning=True)

    DC = net.addDatacenter("DC")

    # add OpenStack-like APIs to the emulated DC
    api1 = OpenstackApiEndpoint("0.0.0.0", 6001)
    api1.connect_datacenter(DC)
    api1.start()
    api1.connect_dc_network(net)

    # add the command line interface endpoint to the emulated DC (REST API)
    rapi1 = RestApiEndpoint("0.0.0.0", 5001)
    rapi1.connectDCNetwork(net)
    rapi1.connectDatacenter(DC)
    rapi1.start()
    
    #Generation Switch
    s1 = net.addSwitch('s1')
    s2 = net.addSwitch('s2')
    s3 = net.addSwitch('s3')
    s4 = net.addSwitch('s4')
    s5 = net.addSwitch('s5')

    S = createHost(httpmode, net, 'S', "host:server")
    # Proxy = createHost(httpmode, net, 'Proxy')
    GI = createHost(httpmode, net, 'GI', "host:gateway")
    GFA = createHost(httpmode, net, 'GFA', "host:gwfinal")
    GFB = createHost(httpmode, net, 'GFB', "host:gwfinal")
    GFC = createHost(httpmode, net, 'GFC', "host:gwfinal")

    #Genration of link
    net.addLink(S, s1)
    net.addLink(GI, s2)
    net.addLink(GFA, s3)
    net.addLink(GFB, s3)
    net.addLink(GFC, s4)

    net.addLink(s1, s2)
    net.addLink(s2, s3)
    net.addLink(s2, s4)
    net.addLink(DC, s4)

    #Do not remove
    net.start()

    #Run gateways and devices
    S.cmd("startup --local_ip 10.0.0.1 --local_port 8080 --local_name srv")
    time.sleep(2)
    GI.cmd("startup --local_ip 10.0.0.2 --local_port 8181 --local_name gwi --remote_ip 10.0.0.1 --remote_port 8080 --remote_name srv")
    time.sleep(2)
    GFA.cmd("startup --local_ip 10.0.0.3 --local_port 8282 --local_name gwfa --remote_ip 10.0.0.2 --remote_port 8181 --remote_name gwi")
    time.sleep(2)
    GFA.cmd("start_devices 10.0.0.3 9001 8282 gwfa 500")
    GFB.cmd("startup --local_ip 10.0.0.4 --local_port 8383 --local_name gwfb --remote_ip 10.0.0.2 --remote_port 8181 --remote_name gwi")
    time.sleep(2)
    GFB.cmd("start_devices 10.0.0.4 9001 8383 gwfb 500")
    GFC.cmd("startup --local_ip 10.0.0.5 --local_port 8484 --local_name gwfc --remote_ip 10.0.0.2 --remote_port 8181 --remote_name gwi")
    
    #Start the command line
    net.CLI()
    # when the user types exit in the CLI, we stop the emulator
    net.stop()


def main():
    args = sys.argv[1:]
    httpmode = len(args)>0 and args[0] == "http"
    create_topology(httpmode)
if __name__ == '__main__':
    main()
