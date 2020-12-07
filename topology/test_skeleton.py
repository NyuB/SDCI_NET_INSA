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
from mininet.log import setLogLevel
from emuvim.DCemulator.net import DCNetwork
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


def create_topology():
    net = DCNetwork(monitor=False, enable_learning=True)

    DC = net.addDatacenter("DC")

    # add OpenStack-like APIs to the emulated DC
    api1 = OpenstackApiEndpoint("0.0.0.0", 6001)
    api1.connect_datacenter(DC)
    api1.start()
    api1.connect_DC_network(net)

    # add the command line interface endpoint to the emulated DC (REST API)
    rapi1 = RestApiEndpoint("0.0.0.0", 5001)
    rapi1.connectDCNetwork(net)
    rapi1.connectDatacenter(DC)
    rapi1.start()
    
    s1 = net.addSwitch('s1')
    s2 = net.addSwitch('s2')
    s3 = net.addSwitch('s3')
    s4 = net.addSwitch('s4')

    S = net.addHost('S')
    GI = net.addHost('GI')
    GFA = net.addHost('GFA')
    GFB = net.addHost('GFA')
    GFB = net.addHost('GFA')

    net.addLink(S, s1, delay = '20ms')
    net.addLink(GI, s2, delay = '20ms')
    net.addLink(GFA, s3, delay = '20ms')
    net.addLink(GFB, s3, delay = '20ms')
    net.addLink(GFC, s4, delay = '20ms')

    net.addLink(s1, s2, delay = '20ms')
    net.addLink(s2, s3, delay = '20ms')
    net.addLink(s2, s4, delay = '20ms')
    net.addLink(s3, s4, delay = '20ms')
    net.addLink(DC, s4, delay = '20ms')

    #Do not remove
    net.start()
    net.CLI()
    # when the user types exit in the CLI, we stop the emulator
    net.stop()


def main():
    create_topology()


if __name__ == '__main__':
    main()